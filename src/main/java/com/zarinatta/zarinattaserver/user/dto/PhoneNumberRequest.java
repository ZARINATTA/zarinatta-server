package com.zarinatta.zarinattaserver.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record PhoneNumberRequest(
        @NotNull(message = "국가 코드는 필수입니다.")
        @Pattern(regexp = "^\\+\\d{1,4}$",
                message = "유효한 나라 코드 형식이 아닙니다. (예: +82, +1, +44)"
        )
        String countryCode,

        @NotNull(message = "전화번호는 필수입니다.")
        @Pattern(
                regexp = "^01[0-9]{8,9}$",
                message = "유효한 전화번호 형식이 아닙니다. (예: 01012345678, 0119876543)"
        )
        String phoneNumber
) {
    public String getFullPhoneNumber() {
        return countryCode + phoneNumber.substring(1);
    }
}
