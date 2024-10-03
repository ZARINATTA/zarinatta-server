package com.zarinatta.zarinattaserver.config;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

        System.out.println("Starting filter for URI: " + httpRequest.getRequestURI());

        boolean isNotFiltered = httpRequest.getRequestURI().contains("/api/v1/ticket") ||
                excludeUrls.contains(httpRequest.getRequestURI());

        System.out.println("isNotFiltered: " + isNotFiltered);

        if (!isNotFiltered) {
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

        // 필터 로직이 완료된 후 요청을 계속 진행
        log.info("Continuing filter chain for URI: " + httpRequest.getRequestURI());
        filterChain.doFilter(httpRequest, httpResponse);
    }
}
