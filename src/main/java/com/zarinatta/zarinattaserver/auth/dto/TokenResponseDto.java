package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        String userNick,
        String userEmail
) {
}
