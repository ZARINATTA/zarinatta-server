package com.zarinatta.zarinattaserver.bookmark.dto.response;

import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import com.zarinatta.zarinattaserver.enums.StationCode;
import lombok.Builder;

@Builder
public record MyBookMarkResponse(
        
        // 티켓 정보
        Long ticketId,
        String ticketType,
        String departDate,
        String departTime,
        StationCode departStation,
        String arriveTime,
        StationCode arriveStation,

        // 즐겨찾기 정보
        Long bookmarkId,
        boolean wantFirstClass,
        SeatLookingFor wantNormalSeat,
        SeatLookingFor wantBabySeat,
        boolean wantWaitingReservation,

        // 즐겨찾기 상태
        BookMarkStatus status
) {
    public static MyBookMarkResponse from(BookMark bookMark) {
        return MyBookMarkResponse.builder()
                .ticketId(bookMark.getTicket().getId())
                .ticketType(bookMark.getTicket().getTicketType())
                .departDate(bookMark.getTicket().getDepartDate())
                .departTime(bookMark.getTicket().getDepartDate() + bookMark.getTicket().getDepartTime() + "0000")
                .departStation(bookMark.getTicket().getDepartStation())
                .arriveTime(bookMark.getTicket().getDepartDate() + bookMark.getTicket().getArriveTime() + "0000")
                .arriveStation(bookMark.getTicket().getArriveStation())
                .bookmarkId(bookMark.getId())
                .wantFirstClass(bookMark.isWantFirstClass())
                .wantNormalSeat(bookMark.getWantNormalSeat())
                .wantBabySeat(bookMark.getWantBabySeat())
                .wantWaitingReservation(bookMark.isWantWaitingReservation())
                .status(bookMark.getStatus())
                .build();
    }
}
