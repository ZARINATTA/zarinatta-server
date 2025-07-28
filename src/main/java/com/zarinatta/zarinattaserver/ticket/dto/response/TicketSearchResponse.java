package com.zarinatta.zarinattaserver.ticket.dto.response;

import lombok.Builder;
import lombok.Data;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.enums.StationCode;

@Data
public class TicketSearchResponse {
    private Long ticketId;
    private String ticketType;
    private String departTime;
    private String arriveTime;
    private StationCode departStation;
    private StationCode arriveStation;
    private String price;

    @Builder
    public TicketSearchResponse(Long ticketId, String ticketType, String departTime, String arriveTime, StationCode departStation, StationCode arriveStation, String price) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.departStation = departStation;
        this.arriveStation = arriveStation;
        this.price = price;
    }

    public static TicketSearchResponse fromEntity(Ticket ticket) {
        return TicketSearchResponse.builder()
                .ticketId(ticket.getId())
                .ticketType(ticket.getTicketType())
                .departTime(ticket.getDepartDate() + ticket.getDepartTime() + "0000")
                .arriveTime(ticket.getDepartDate() + ticket.getArriveTime() + "0000")
                .departStation(ticket.getDepartStation())
                .arriveStation(ticket.getArriveStation())
                .price(ticket.getPrice()).build();
    }
}
