package com.zarinatta.zarinattaserver.config;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TokenValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private List<String> excludeUrls = new ArrayList<>();

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isNotFiltered = excludeUrls.contains(httpRequest.getRequestURI());

        if (!isNotFiltered && !httpRequest.getMethod().equals("OPTIONS")) {
            log.info("[TokenValidationFilter] Filter is executing");
            Cookie[] cookies = httpRequest.getCookies();
            String accessToken = null;

            if (cookies != null && cookies.length > 0) {
                log.info("[TokenValidationFilter] Cookies found: " + cookies.length);
                accessToken = Arrays.stream(cookies)
                        .filter(cookie -> "skt".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
            } else {
                log.error("[TokenValidationFilter] No cookies found in the request.");
            }

            if (accessToken == null) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
                return; // 응답을 보낸 후 메서드 종료
            }

            String userId = jwtService.decodeAccessToken(accessToken);

            httpRequest.setAttribute("accessToken", accessToken);
            httpRequest.setAttribute("userId", userId);
            log.info("[TokenValidationFilter] Access token and userId set in request");
        }
        filterChain.doFilter(httpRequest, httpResponse);
    }
}
