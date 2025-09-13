package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record UserInfoDto(
        String userNick,
        String userEmail
) {
}
