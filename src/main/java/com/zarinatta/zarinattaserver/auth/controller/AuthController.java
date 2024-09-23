package com.zarinatta.zarinattaserver.auth.controller;

import com.zarinatta.zarinattaserver.auth.dto.MasterTokenDto;
import com.zarinatta.zarinattaserver.auth.dto.RedirectDto;
import com.zarinatta.zarinattaserver.auth.dto.TestDto;
import com.zarinatta.zarinattaserver.auth.dto.TokenResponseDto;
import com.zarinatta.zarinattaserver.auth.service.AuthService;
import com.zarinatta.zarinattaserver.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/redirect")
    public ResponseEntity<RedirectDto> redirect() throws IOException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(authService.redirect2());
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> signup(@RequestParam String code) throws Exception {
        TokenResponseDto tokenResponseDto = authService.signup2(code);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", tokenResponseDto.getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(30 * 60L)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(Map.of("refreshToken", tokenResponseDto.getRefreshToken(), "userEmail", tokenResponseDto.getUserEmail(), "userNick", tokenResponseDto.getUserNick()));
    }

    @PostMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorize(HttpServletRequest request, @RequestBody Map<String, String> map) throws Exception {
        String accessToken = (String) request.getAttribute("accessToken");
        String refreshToken = map.get("refreshToken");

        TokenResponseDto tokenResponseDto = authService.authorize(accessToken, refreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", tokenResponseDto.getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(30 * 60L)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(Map.of("refreshToken", tokenResponseDto.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = (String) request.getAttribute("accessToken");

        authService.logout(accessToken);

        log.info("[AuthController-logout] : accessToken -> "+accessToken);

        ResponseCookie deletedCookie = ResponseCookie.from("skt", "")
                .maxAge(0) // 쿠키 만료 시간 설정
                .path("/") // 쿠키의 경로 설정
                .httpOnly(true) // 보안 설정
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", deletedCookie.toString()).build();
    }

    @GetMapping("/master")
    public ResponseEntity<Map<String, String>> generateMasterToken(HttpServletResponse response) {

        MasterTokenDto masterTokenDto = jwtService.createMasterToken();

        ResponseCookie cookie = ResponseCookie.from("skt", masterTokenDto.getAccessToken())
                .maxAge(-1) // 쿠키 만료 시간 설정
                .path("/") // 쿠키의 경로 설정
                .httpOnly(true) // 보안 설정
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", masterTokenDto.getAccessToken())
                .body(Map.of("refreshToken", masterTokenDto.getRefreshToken()));
    }

    @GetMapping("/test")
    public ResponseEntity<TestDto> test() {
        TestDto testDto = TestDto.builder().message("hello kim!").build();
        return new ResponseEntity<>(testDto, HttpStatusCode.valueOf(200));
    }
}
