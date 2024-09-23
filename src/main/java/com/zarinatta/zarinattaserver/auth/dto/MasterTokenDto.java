package com.zarinatta.zarinattaserver.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MasterTokenDto {
    String accessToken;
    String refreshToken;
}
