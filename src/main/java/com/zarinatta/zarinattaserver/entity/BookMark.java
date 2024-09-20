package com.zarinatta.zarinattaserver.entity;

import com.zarinatta.zarinattaserver.bookmark.dto.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @Column
    private boolean wantFirstClass;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatLookingFor wantNormalSeat;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatLookingFor wantBabySeat;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean wantWaitingReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public BookMark(boolean wantFirstClass, SeatLookingFor wantNormalSeat, SeatLookingFor wantBabySeat, boolean wantWaitingReservation, Ticket ticket, User user) {
        this.wantFirstClass = wantFirstClass;
        this.wantNormalSeat = wantNormalSeat;
        this.wantBabySeat = wantBabySeat;
        this.wantWaitingReservation = wantWaitingReservation;
        this.ticket = ticket;
        this.user = user;
    }

    public static BookMark from(BookMarkCreateRequest request, Ticket ticket, User user) {
        return BookMark.builder()
                .wantFirstClass(request.getWantFirstClass())
                .wantNormalSeat(request.getWantNormalSeat())
                .wantBabySeat(request.getWantBabySeat())
                .wantWaitingReservation(request.getWantWaitingReservation())
                .ticket(ticket)
                .user(user)
                .build();
    }
}
