package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.bookmark.repository.BookMarkRepository;
import com.zarinatta.zarinattaserver.bookmark.service.BookMarkService;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import com.zarinatta.zarinattaserver.enums.StationCode;
import com.zarinatta.zarinattaserver.exception.exception.NotPermit.BookMarkPermitException;
import com.zarinatta.zarinattaserver.ticket.repository.TicketRepository;
import com.zarinatta.zarinattaserver.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Profile("test")
class BookMarkServiceTest {

    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    private BookMarkRepository bookMarkRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        bookMarkRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("기차표를 즐겨찾기에 등록한다.")
    public void ID가1인유저가_ID가2인티켓을_즐겨찾기에_등록(){
        //given
        createDummyTicket();
        createDummyUser();
        BookMarkCreateRequest request = BookMarkCreateRequest.builder()
                .ticketId(2L)
                .wantFirstClass(false)
                .wantNormalSeat(SeatLookingFor.STANDING_SEAT)
                .wantBabySeat(SeatLookingFor.SEAT)
                .wantWaitingReservation(true)
                .build();

        //when
        bookMarkService.createBookMark(userRepository.findById("1").get(), request);

        //then
        BookMark bookMark = bookMarkRepository.findById(1L).get();
        assertEquals(bookMark.getTicket().getId(), 2L);
        assertEquals(bookMark.getUser().getId(), "1");
        assertEquals(bookMark.isWantFirstClass(), false);
        assertEquals(bookMark.getWantNormalSeat(), SeatLookingFor.STANDING_SEAT);
        assertEquals(bookMark.getWantBabySeat(), SeatLookingFor.SEAT);
        assertEquals(bookMark.isWantWaitingReservation(), true);
    }

    @Test
    @DisplayName("등록된 즐겨찾기를 삭제한다.")
    public void ID가1인유저가_ID가2인_본인즐겨찾기_삭제(){
        //given
        createDummyData();

        //when
        bookMarkService.deleteBookMark(userRepository.findById("1").get(), 2L);

        //then
        List<BookMark> bookMarks = bookMarkRepository.findAll();
        assertThat(bookMarks.size()).isEqualTo(2);
        assertThat(bookMarks.get(0).getTicket().getId()).isEqualTo(1L);
        assertThat(bookMarks.get(1).getTicket().getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("자기 자신의 즐겨찾기만 삭제 할수 있다.")
    public void ID가2인유저가_ID가2인_다른유저의_즐겨찾기_삭제(){
        //given
        createDummyData();

        //when //then
        assertThrows(BookMarkPermitException.class,
                () -> bookMarkService.deleteBookMark(userRepository.findById("2").get(), 2L));
    }

    void createDummyData() {
        createDummyTicket();
        createDummyUser();
        createDummyBookMark();
    }

    @Transactional
    void createDummyTicket() {
        List<Ticket> tickets = Arrays.asList(
                new Ticket("새마을호 1001", "20240807", "060000", StationCode.부산, "100000", StationCode.수원, "25000원"),
                new Ticket("KTX 101", "20240807", "070000", StationCode.부산, "100000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2010", "20240807", "080000", StationCode.부산, "110000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1002", "20240807", "090000", StationCode.부산, "130000", StationCode.수원, "25000원"),
                new Ticket("KTX 102", "20240807", "100000", StationCode.부산, "130000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2011", "20240807", "110000", StationCode.부산, "140000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1003", "20240807", "120000", StationCode.부산, "160000", StationCode.수원, "25000원"),
                new Ticket("KTX 103", "20240807", "130000", StationCode.부산, "160000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2012", "20240807", "140000", StationCode.부산, "170000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1004", "20240807", "150000", StationCode.부산, "190000", StationCode.수원, "25000원")
        );
        ticketRepository.saveAll(tickets);
    }

    @Transactional
    void createDummyUser() {
        List<User> users = List.of(
                User.builder()
                .id("1")
                .userNick("test")
                .userEmail("ee@gmail.com")
                .build(),
                User.builder()
                .id("2")
                .userNick("test2")
                .userEmail("ee2@gmail.com")
                .build());
        userRepository.saveAll(users);
    }

    @Transactional
    void createDummyBookMark() {
        User user = userRepository.findById("1").get();
        List<Ticket> tickets = ticketRepository.findAll();
        List<BookMark> bookMarks = Arrays.asList(
                BookMark.builder()
                        .wantFirstClass(true)
                        .wantNormalSeat(SeatLookingFor.SEAT)
                        .wantBabySeat(SeatLookingFor.SEAT)
                        .wantWaitingReservation(false)
                        .ticket(tickets.get(0))
                        .user(user)
                        .build(),
                BookMark.builder()
                        .wantFirstClass(false)
                        .wantNormalSeat(SeatLookingFor.SEAT)
                        .wantBabySeat(SeatLookingFor.SEAT)
                        .wantWaitingReservation(true)
                        .ticket(tickets.get(1))
                        .user(user)
                        .build(),
                BookMark.builder()
                        .wantFirstClass(true)
                        .wantNormalSeat(SeatLookingFor.SEAT)
                        .wantBabySeat(SeatLookingFor.SEAT)
                        .wantWaitingReservation(true)
                        .ticket(tickets.get(2))
                        .user(user)
                        .build()
        );
        bookMarkRepository.saveAll(bookMarks);
    }
}