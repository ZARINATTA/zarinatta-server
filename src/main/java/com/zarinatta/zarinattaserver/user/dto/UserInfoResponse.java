package com.zarinatta.zarinattaserver.user.dto;

import lombok.Builder;

@Builder
public record UserInfoResponse(
        String id,
        String email,
        String nickname,
        String phoneNumber
) {
}
