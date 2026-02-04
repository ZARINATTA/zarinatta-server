package com.zarinatta.zarinattaserver.config;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> filters(JwtService jwtService) {
        FilterRegistrationBean<TokenValidationFilter> tokenValidationFilterRegistrationBean = new FilterRegistrationBean<>();

        TokenValidationFilter tokenValidationFilter = new TokenValidationFilter(jwtService);
        tokenValidationFilter.setExcludeUrls(Arrays.asList(
                "/api/v1/auth/redirect",
                "/api/v1/auth/login",
                "/api/v1/auth/master",
                "/api/v1/ticket/search",
                "/api/v1/ticket",
                "/auth/test",
                "/api/v1/station/search",
                "/api/v1/station/frequent",
                "/api/v1/bookmark/search",
                "/actuator/prometheus",
                "/actuator/health"));
        tokenValidationFilterRegistrationBean.setFilter(tokenValidationFilter);
        tokenValidationFilterRegistrationBean.setOrder(1);

        return tokenValidationFilterRegistrationBean;
    }
}

