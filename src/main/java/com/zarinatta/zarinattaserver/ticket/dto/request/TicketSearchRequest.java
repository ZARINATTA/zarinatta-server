package com.zarinatta.zarinattaserver.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.zarinatta.zarinattaserver.enums.StationCode;

@Data
@NoArgsConstructor
public class TicketSearchRequest {
    @NotNull(message = "출발역 입력 필수")
    private StationCode departStation;
    @NotNull(message = "도착역 입력 필수")
    private StationCode arriveStation;
    @NotBlank(message = "출발 날짜 입력 필수")
    private String departDate;
    @NotBlank(message = "출발 시간 입력 필수")
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
