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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenValidationFilter implements Filter {

    private final JwtService jwtService;
    private List<String> excludeUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업이 필요하면 여기에 작성
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ZarinattaException, IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("왜안돼이런michin");

        if(httpRequest.getRequestURI().contains("/api/v1/ticket")) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        if(excludeUrls.contains(httpRequest.getRequestURI())) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

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

        if (accessToken != null) {
            String userId = jwtService.decodeAccessToken(accessToken);

            //if (userId != null) {
                httpRequest.setAttribute("accessToken", accessToken);
                httpRequest.setAttribute("userId", userId);
                log.info("[TokenValidationFilter] Access token and userId set in request");
                chain.doFilter(httpRequest, httpResponse);
                return;
            //}
        }

        chain.doFilter(httpRequest, httpResponse); // 필터 체인을 계속 실행

        // 유효하지 않은 토큰인 경우 401 에러 반환
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
