package com.zarinatta.zarinattaserver.config;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class FilterConfig {

    @Bean
    public List<FilterRegistrationBean<?>> filters(JwtService jwtService) {
        FilterRegistrationBean<TokenValidationFilter> tokenValidationFilterRegistrationBean = new FilterRegistrationBean<>();
        TokenValidationFilter tokenValidationFilter = new TokenValidationFilter(jwtService);
        tokenValidationFilter.setExcludeUrls(Arrays.asList("/api/v1/auth/redirect", "/api/v1/auth/signup", "/api/v1/ticket/search", "/auth/test", "/api/v1/station/search", "/api/v1/station/frequent"));
        tokenValidationFilterRegistrationBean.setFilter(tokenValidationFilter);

        FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterRegistrationBean = new FilterRegistrationBean<>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilterRegistrationBean.setFilter(characterEncodingFilter);

        return Arrays.asList(tokenValidationFilterRegistrationBean, characterEncodingFilterRegistrationBean);
    }
}

