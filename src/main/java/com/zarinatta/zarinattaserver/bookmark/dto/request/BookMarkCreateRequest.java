package com.zarinatta.zarinattaserver.bookmark.dto.request;

import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookMarkCreateRequest {

    @NotNull(message = "티켓 ID는 필수입니다.")
    private Long ticketId;
    @NotNull(message = "특실 즐겨찾기 여부는 필수입니다.")
    private Boolean wantFirstClass;
    @NotNull(message = "일반실 즐겨찾기 여부는 필수입니다.")
    private SeatLookingFor wantNormalSeat;
    @NotNull(message = "유아실 즐겨찾기 여부는 필수입니다.")
    private SeatLookingFor wantBabySeat;
    @NotNull(message = "예약 대기 즐겨찾기 여부는 필수입니다.")
    private Boolean wantWaitingReservation;

    @Builder
    public BookMarkCreateRequest(Long ticketId, Boolean wantFirstClass, SeatLookingFor wantNormalSeat, SeatLookingFor wantBabySeat, Boolean wantWaitingReservation) {
        this.ticketId = ticketId;
        this.wantFirstClass = wantFirstClass;
        this.wantNormalSeat = wantNormalSeat;
        this.wantBabySeat = wantBabySeat;
        this.wantWaitingReservation = wantWaitingReservation;
    }
}
