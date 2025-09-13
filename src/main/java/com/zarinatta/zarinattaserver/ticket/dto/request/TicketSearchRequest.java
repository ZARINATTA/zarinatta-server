package com.zarinatta.zarinattaserver.ticket.dto.request;

import com.zarinatta.zarinattaserver.enums.StationCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TicketSearchRequest(
        @NotNull(message = "출발역 입력 필수")
        StationCode departStation,
        @NotNull(message = "도착역 입력 필수")
        StationCode arriveStation,
        @NotBlank(message = "출발 날짜 입력 필수")
        @Pattern(regexp = "^\\d{8}$", message = "날짜 값은 'YYYYMMDD' 형식이어야 합니다.")
        String departDate,
        String departTime,
        String trainType
) {
}
