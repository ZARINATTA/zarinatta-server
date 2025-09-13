package com.zarinatta.zarinattaserver.auth.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zarinatta.zarinattaserver.auth.dto.KakaoProfileDto;
import com.zarinatta.zarinattaserver.auth.dto.LoginDto;
import com.zarinatta.zarinattaserver.auth.dto.RedirectDto;
import com.zarinatta.zarinattaserver.auth.dto.TokenResponseDto;
import com.zarinatta.zarinattaserver.config.RedisService;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.user.dto.UserInputDto;
import com.zarinatta.zarinattaserver.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${KAKAO_CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${KAKAO_REDIRECT_URI}")
    private String REDIRECT_URI;

    @Value("${KAKAO_REDIRECT_URI_LOCAL}")
    private String REDIRECT_LOCAL_URI;

    @Value("${KAKAO_JWT_NONCE}")
    private String nonce;

    private final static long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    private final UserService userService;

    private final JwtService jwtService;

    private final RedisService redisService;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public RedirectDto redirect2(HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        String origin = httpServletRequest.getHeader("Origin");

        String redirectOriginUri = origin.startsWith("http://localhost:3000") ? REDIRECT_LOCAL_URI : REDIRECT_URI;

        String requestUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + redirectOriginUri + "&nonce=" + nonce;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUri))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        String redirectUri = handleResponse(response);

       return RedirectDto.builder().redirectUri(redirectUri).build();
    }

    @Transactional
    public TokenResponseDto signup2(String code) throws ZarinattaException, IOException, InterruptedException {
        String getTokenUri = "https://kauth.kakao.com/oauth/token";

        Map<String, String> formData = Map.of(
                "grant_type", "authorization_code",
                "client_id", CLIENT_ID,
                "code", code,
                "redirect_uri", REDIRECT_URI
        );

        String encodedFormData = formData.entrySet()
                .stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getTokenUri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(encodedFormData))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            log.error("[AuthService-sinup2] : LoginDto get error");
            System.out.println(response.statusCode());
            System.out.println(response.body().toString());
            throw new ZarinattaException(ErrorCode.KAKAO_SERVER_ERROR);
        }

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        LoginDto loginDto = objectMapper.readValue(response.body(), LoginDto.class);

        log.info("------------------[LoginDto Get]------------------");

        String getUserInfoUri = "https://kapi.kakao.com/v2/user/me";

        request = HttpRequest.newBuilder()
                .uri(URI.create(getUserInfoUri))
                .header("Authorization", "Bearer " + loginDto.access_token())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.noBody()) // 빈 본문을 전송 (폼 데이터가 필요 없는 경우)
                .build();

        // 요청을 보내고 응답 받기
        HttpResponse<String> userInfoResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 상태 코드 검사 및 에러 처리
        if (userInfoResponse.statusCode() >= 400) {
            throw new ZarinattaException(ErrorCode.KAKAO_SERVER_ERROR);
        }

        KakaoProfileDto kakaoProfileDto = objectMapper.readValue(userInfoResponse.body(), KakaoProfileDto.class);

        log.info("------------------[KakaoProfileDto Get]------------------");

        String email = kakaoProfileDto.kakao_account().email();
        String nickname = kakaoProfileDto.kakao_account().profile().nickname();

        String userId = userService.findUserIdByEmail(email);

        if(userService.findUserIdByEmail(email) == null) {
            userId = userService.save(UserInputDto.builder()
                    .userEmail(email)
                    .userNick(nickname)
                    .build());
        }

        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken();

        redisService.setValue(refreshToken, userId, REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).userNick(nickname).userEmail(email).build();
    }

    public TokenResponseDto authorize(String accessToken, String refreshToken) throws ZarinattaException {
        String userId = redisService.getValue(refreshToken);

        if(userId == null) {
            this.logout(accessToken);
            throw new ZarinattaException(ErrorCode.EXPIRED_TOKEN_ERROR);
        }

        String newAccessToken = jwtService.createAccessToken(userId);
        String newRefreshToken = jwtService.createRefreshToken();

        redisService.deleteValue(refreshToken);
        redisService.setValue(newRefreshToken, userId, REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

    public void logout(String accessToken) throws ZarinattaException {
        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        redisService.deleteValue(userId);
    }

    private static String handleResponse(HttpResponse<Void> response) throws ZarinattaException {
        if (response.statusCode() == 302) {
            HttpHeaders headers = response.headers();
            return headers.firstValue("Location").orElseThrow(() ->
                    new ZarinattaException(ErrorCode.AUTH_SERVER_ERROR));
        }

        throw new ZarinattaException(ErrorCode.AUTH_SERVER_ERROR);
    }
}
