package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record LoginDto(
        String token_type,
        String id_token,
        String access_token,
        int expires_in,
        String refresh_token,
        int refresh_token_expires_in,
        String scope
) {
}
