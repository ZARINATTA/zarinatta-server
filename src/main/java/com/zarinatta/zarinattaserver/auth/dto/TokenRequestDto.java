package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record TokenRequestDto(
        String grant_type,
        String client_id,
        String code,
        String redirect_uri
) {
}
