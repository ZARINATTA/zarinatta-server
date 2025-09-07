package com.zarinatta.zarinattaserver.bookmark.dto.request;

import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookMarkCreateRequest(
        @NotNull(message = "티켓 ID는 필수입니다.")
        Long ticketId,
        @NotNull(message = "특실 즐겨찾기 여부는 필수입니다.")
        Boolean wantFirstClass,
        @NotNull(message = "일반실 즐겨찾기 여부는 필수입니다.")
        SeatLookingFor wantNormalSeat,
        @NotNull(message = "유아실 즐겨찾기 여부는 필수입니다.")
        SeatLookingFor wantBabySeat,
        @NotNull(message = "예약 대기 즐겨찾기 여부는 필수입니다.")
        Boolean wantWaitingReservation
) {
}
