package com.zarinatta.zarinattaserver.ticket.controller;

import com.zarinatta.zarinattaserver.ticket.dto.request.TicketSearchRequest;
import com.zarinatta.zarinattaserver.ticket.dto.response.PageTicketResponse;
import com.zarinatta.zarinattaserver.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageTicketResponse searchTicket(@Valid TicketSearchRequest ticketSearchRequest, Pageable pageable) {
        return ticketService.getTicket(ticketSearchRequest, pageable);
    }

    @PostMapping("/mock")
    @ResponseStatus(HttpStatus.OK)
    public void insertData() {
        ticketService.insertMockData();
    }

    @GetMapping("/sentry/test")
    public void test() {
        throw new RuntimeException("sentry 예외 테스트 - 시간 : " + System.currentTimeMillis());}
}
