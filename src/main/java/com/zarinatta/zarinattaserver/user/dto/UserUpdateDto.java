package com.zarinatta.zarinattaserver.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateDto(
        String userDeviceToken,
        String userPhone
) {
}
