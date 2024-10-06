package com.zarinatta.zarinattaserver.ticket.dto.request;

import com.zarinatta.zarinattaserver.enums.StationCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketSearchRequest {
    @NotNull(message = "출발역 입력 필수")
    private StationCode departStation;
    @NotNull(message = "도착역 입력 필수")
    private StationCode arriveStation;
    @NotBlank(message = "출발 날짜 입력 필수")
    @Pattern(regexp = "^\\d{8}$", message = "날짜 값은 'YYYYMMDD' 형식이어야 합니다.")
    private String departDate;
    @NotBlank(message = "출발 시간 입력 필수")
    @Pattern(regexp = "^\\d{4}$", message = "시간 값은 'MMSS' 형식이어야 합니다.")
    private String departTime;
    private String trainType;

    @Builder
    public TicketSearchRequest(StationCode departStation, StationCode arriveStation, String departDate, String departTime, String trainType) {
        this.departStation = departStation;
        this.arriveStation = arriveStation;
        this.departDate = departDate;
        this.departTime = departTime;
        this.trainType = trainType;
    }
}
