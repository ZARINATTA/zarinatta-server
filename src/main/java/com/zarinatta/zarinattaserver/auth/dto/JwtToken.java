package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record JwtToken(
        String nickname,
        String email
) {
}