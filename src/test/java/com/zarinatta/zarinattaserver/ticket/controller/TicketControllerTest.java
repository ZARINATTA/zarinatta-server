package com.zarinatta.zarinattaserver.ticket.controller;

import com.zarinatta.zarinattaserver.ticket.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers  = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    @DisplayName("출발역, 도착역, 출발일시, 열차 종류로 티켓을 검색한다.")
    public void searchTicket() throws Exception {
        //given //when //then
        mockMvc.perform(
                get("/api/v1/ticket/search")
                        .param("departStation", "가남")
                        .param("arriveStation", "순천")
                        .param("departDate", "20240101")
                        .param("departTime", "0000")
                        .param("trainType", "KTX")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}