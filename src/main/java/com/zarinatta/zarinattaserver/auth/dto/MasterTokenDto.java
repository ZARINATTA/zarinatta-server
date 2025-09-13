package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record MasterTokenDto(
        String accessToken,
        String refreshToken
) {
}
