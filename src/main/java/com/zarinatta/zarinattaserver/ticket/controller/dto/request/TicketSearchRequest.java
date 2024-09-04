package com.zarinatta.zarinattaserver.ticket.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.zarinatta.zarinattaserver.enums.StationCode;

@Data
@NoArgsConstructor
public class TicketSearchRequest {
    @NotNull
    private StationCode departStation;
    @NotNull
    private StationCode arriveStation;
    private String departDate;
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
