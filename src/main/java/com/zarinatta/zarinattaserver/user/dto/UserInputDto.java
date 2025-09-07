package com.zarinatta.zarinattaserver.user.dto;

import lombok.Builder;

@Builder
public record UserInputDto(
        String userEmail,
        String userNick
) {
}