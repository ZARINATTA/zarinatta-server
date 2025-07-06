package com.zarinatta.zarinattaserver.bookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkCreateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.BookMarkStatusUpdateRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.request.MyBookMarkRequest;
import com.zarinatta.zarinattaserver.bookmark.dto.response.MyBookMarkPageResponse;
import com.zarinatta.zarinattaserver.bookmark.service.BookMarkService;
import com.zarinatta.zarinattaserver.entity.BookMark;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import com.zarinatta.zarinattaserver.enums.SeatLookingFor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookMarkController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookMarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BookMarkService bookMarkService;

    @MockBean
    private com.zarinatta.zarinattaserver.bookmark.repository.BookMarkRepository bookMarkRepository;

    private final String accessToken = "dummyToken";

    @Test
    @DisplayName("기차 검색 할때 회원 유저의 즐겨찾기 한 열차 목록을 반환한다.")
    public void searchBookMark() throws Exception {
        // given
        User dummyUser = User.builder().id("user1").build();
        List<Long> ticketIds = Arrays.asList(1L, 2L);
        Ticket dummyTicket = Ticket.builder()
                .departDate("2023-10-10")
                .departTime("10:00")
                .arriveTime("12:00")
                .price("100")
                .build();
        ReflectionTestUtils.setField(dummyTicket, "id", 1L);
        BookMark dummyBookMark = BookMark.builder()
                .ticket(dummyTicket)
                .build();
        ReflectionTestUtils.setField(dummyBookMark, "id", 1L);
        List<BookMark> bookMarks = Arrays.asList(dummyBookMark);
        Mockito.when(jwtService.findUserByToken(accessToken)).thenReturn(Optional.of(dummyUser));
        Mockito.when(bookMarkRepository.findAllByTicketIdInAndUserId(eq(ticketIds), eq(dummyUser))).thenReturn(bookMarks);

        // when // then
        mockMvc.perform(get("/api/v1/bookmark/search")
                        .requestAttr("accessToken", accessToken)
                        .param("ticketIds", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookmarkId").value(1))
                .andExpect(jsonPath("$[0].ticketId").value(1));
    }

    @Test
    @DisplayName("회원 유저가 열차를 즐겨찾기에 추가한다.")
    public void createBookMark() throws Exception {
        // given
        User dummyUser = User.builder().id("user1").build();
        BookMarkCreateRequest request = BookMarkCreateRequest.builder()
                .ticketId(1L)
                .wantFirstClass(true)
                .wantNormalSeat(SeatLookingFor.SEAT)
                .wantBabySeat(SeatLookingFor.SEAT)
                .wantWaitingReservation(true)
                .build();
        Mockito.when(jwtService.findUserByToken(accessToken)).thenReturn(Optional.of(dummyUser));
        // when // then
        mockMvc.perform(post("/api/v1/bookmark/create")
                        .requestAttr("accessToken", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("TicketId: 1 - 즐겨찾기 추가 완료"));
    }

    @Test
    @DisplayName("회원 유저가 즐겨찾기를 삭제한다.")
    public void deleteBookMark() throws Exception {
        // given
        User dummyUser = User.builder().id("user1").build();
        Mockito.when(jwtService.findUserByToken(accessToken)).thenReturn(Optional.of(dummyUser));
        Long bookMarkId = 1L;
        // when // then
        mockMvc.perform(delete("/api/v1/bookmark/delete/{bookMarkId}", bookMarkId)
                        .requestAttr("accessToken", accessToken))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원 유저의 전체 즐겨찾기 리스트를 조회한다.")
    public void getMyBookMarkList() throws Exception {
        // given
        User dummyUser = User.builder().id("user1").build();
        MyBookMarkRequest request = new MyBookMarkRequest();
        request.setExpire(false);
        MyBookMarkPageResponse pageResponse = MyBookMarkPageResponse.builder()
                .responseList(Arrays.asList())
                .page(1)
                .totalDataCount(0)
                .totalPageCount(0)
                .build();
        Mockito.when(jwtService.findUserByToken(accessToken)).thenReturn(Optional.of(dummyUser));
        Mockito.when(bookMarkService.getMyBookMark(eq(dummyUser), any(MyBookMarkRequest.class)))
                .thenReturn(pageResponse);
        // when // then
        mockMvc.perform(get("/api/v1/bookmark/list")
                        .requestAttr("accessToken", accessToken)
                        .param("expire", "false")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 유저의 즐겨찾기 상태를 예매 성공/실패로 업데이트")
    public void updateBookMarkState() throws Exception {
        // given
        User dummyUser = User.builder().id("user1").build();
        BookMarkStatusUpdateRequest request = new BookMarkStatusUpdateRequest();
        request.setBookMarkId(1L);
        request.setUpdateStatus(BookMarkStatus.UNKNOWN);
        Mockito.when(jwtService.findUserByToken(accessToken)).thenReturn(Optional.of(dummyUser));
        Mockito.when(bookMarkService.updateBookMarkStatus(any(BookMarkStatusUpdateRequest.class)))
                .thenReturn(BookMarkStatus.UNKNOWN);
        // when // then
        mockMvc.perform(post("/api/v1/bookmark/status")
                        .requestAttr("accessToken", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}