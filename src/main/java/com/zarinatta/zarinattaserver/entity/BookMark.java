package com.zarinatta.zarinattaserver.entity;

import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @Column(name = "IS_SENT", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean isSent;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookMarkStatus status;

    @Column(name = "WANT_FIRST_CLASS", nullable = false)
    private boolean wantFirstClass;

    @Column(name = "WANT_NORMAL_SEAT", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatLookingFor wantNormalSeat;

    @Column(name = "WANT_BABY_SEAT", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatLookingFor wantBabySeat;

    @Column(name = "WANT_WAITING_RESERVATION", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean wantWaitingReservation;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "IS_DELETED", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Builder
    public BookMark(boolean isSent, BookMarkStatus status, boolean wantFirstClass, SeatLookingFor wantNormalSeat, SeatLookingFor wantBabySeat, boolean wantWaitingReservation, LocalDateTime createdAt, Boolean isDeleted, Ticket ticket, User user) {
        this.isSent = isSent;
        this.status = status;
        this.wantFirstClass = wantFirstClass;
        this.wantNormalSeat = wantNormalSeat;
        this.wantBabySeat = wantBabySeat;
        this.wantWaitingReservation = wantWaitingReservation;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.ticket = ticket;
        this.user = user;
    }

    public static BookMark from(BookMarkCreateRequest request, Ticket ticket, User user) {
        return BookMark.builder()
                .isSent(false)
                .status(BookMarkStatus.UNKNOWN)
                .wantFirstClass(request.wantFirstClass())
                .wantNormalSeat(request.wantNormalSeat())
                .wantBabySeat(request.wantBabySeat())
                .wantWaitingReservation(request.wantWaitingReservation())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .ticket(ticket)
                .user(user)
                .build();
    }

    //==비즈니스 로직==//
    public BookMarkStatus updateStatus(BookMarkStatus bookMarkStatus) {
        this.status = bookMarkStatus;
        return this.status;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
