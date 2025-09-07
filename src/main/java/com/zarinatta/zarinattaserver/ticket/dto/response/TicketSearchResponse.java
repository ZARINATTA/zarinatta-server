package com.zarinatta.zarinattaserver.ticket.dto.response;

import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.enums.StationCode;
import lombok.Builder;

@Builder
public record TicketSearchResponse(
        Long ticketId,
        String ticketType,
        String departTime,
        String arriveTime,
        StationCode departStation,
        StationCode arriveStation,
        String price
) {
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
