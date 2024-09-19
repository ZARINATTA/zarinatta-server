package com.zarinatta.zarinattaserver.bookmark;

import com.zarinatta.zarinattaserver.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import com.zarinatta.zarinattaserver.enums.StationCode;
import com.zarinatta.zarinattaserver.ticket.repository.TicketRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Profile("test")
class BookMarkRepositoryTest {
    @Autowired
    private BookMarkRepository bookMarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("티켓 Id List와 User Entity를 통해 티켓 id 중에 즐겨 찾기 한 항목을 가져 온다.")
    public void getBookMarks() {
        //given
        creteDummyData();

        //when
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(Arrays.asList(1L, 2L, 3L), userRepository.findById("1").get());

        //then
        Assertions.assertEquals(bookMarks.size(), 3);
        Assertions.assertEquals(bookMarks.get(0).getTicket().getId(), 1L);
    }

    void creteDummyData() {
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
        User user = User.builder()
                .id("1")
                .userNick("test")
                .userEmail("ee@gmail.com")
                .build();
        userRepository.save(user);
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