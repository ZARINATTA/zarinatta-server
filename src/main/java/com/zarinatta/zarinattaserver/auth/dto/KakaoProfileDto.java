package com.zarinatta.zarinattaserver.auth.dto;

import lombok.Builder;

@Builder
public record KakaoProfileDto(
        long id,
        String connected_at,
        Properties properties,
        KakaoAccount kakao_account
) {
    @Builder
    public record Properties(
            String nickname
    ) {
    }

    @Builder
    public record KakaoAccount(
            boolean profile_nickname_needs_agreement,
            Profile profile,
            boolean has_email,
            boolean email_needs_agreement,
            boolean is_email_valid,
            boolean is_email_verified,
            String email
    ) {
        @Builder
        public record Profile(
                String nickname,
                boolean is_default_nickname
        ) {
        }
    }
}
