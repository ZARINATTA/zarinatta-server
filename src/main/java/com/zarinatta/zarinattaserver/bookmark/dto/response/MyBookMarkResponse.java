package com.zarinatta.zarinattaserver.bookmark.dto.response;

import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import com.zarinatta.zarinattaserver.enums.StationCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyBookMarkResponse {
    // 티켓 정보
    private Long ticketId;
    private String ticketType;
    private String departDate;
    private String departTime;
    private StationCode departStation;
    private String arriveTime;
    private StationCode arriveStation;
    // 즐겨찾기 정보
    private Long bookmarkId;
    private boolean wantFirstClass;
    private SeatLookingFor wantNormalSeat;
    private SeatLookingFor wantBabySeat;
    private boolean wantWaitingReservation;

    @Builder
    public MyBookMarkResponse(Long ticketId, String ticketType, String departDate, String departTime, StationCode departStation, String arriveTime, StationCode arriveStation, Long bookmarkId, boolean wantFirstClass, SeatLookingFor wantNormalSeat, SeatLookingFor wantBabySeat, boolean wantWaitingReservation) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departDate = departDate;
        this.departTime = departTime;
        this.departStation = departStation;
        this.arriveTime = arriveTime;
        this.arriveStation = arriveStation;
        this.bookmarkId = bookmarkId;
        this.wantFirstClass = wantFirstClass;
        this.wantNormalSeat = wantNormalSeat;
        this.wantBabySeat = wantBabySeat;
        this.wantWaitingReservation = wantWaitingReservation;
    }

    public static MyBookMarkResponse from(BookMark bookMark) {
        return MyBookMarkResponse.builder()
                .ticketId(bookMark.getTicket().getId())
                .ticketType(bookMark.getTicket().getTicketType())
                .departDate(bookMark.getTicket().getDepartDate())
                .departTime(bookMark.getTicket().getDepartTime())
                .departStation(bookMark.getTicket().getDepartStation())
                .arriveTime(bookMark.getTicket().getArriveTime())
                .arriveStation(bookMark.getTicket().getArriveStation())
                .bookmarkId(bookMark.getId())
                .wantFirstClass(bookMark.isWantFirstClass())
                .wantNormalSeat(bookMark.getWantNormalSeat())
                .wantBabySeat(bookMark.getWantBabySeat())
                .wantWaitingReservation(bookMark.isWantWaitingReservation())
                .build();
    }
}
