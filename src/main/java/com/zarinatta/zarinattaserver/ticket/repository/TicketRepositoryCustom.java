package com.zarinatta.zarinattaserver.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.ticket.controller.dto.request.TicketSearchRequest;

public interface TicketRepositoryCustom {
    Page<Ticket> findTicketBySearchDTO(TicketSearchRequest ticketSearchRequest, Pageable pageable);
    Long countAll(TicketSearchRequest ticketSearchRequest);
    void insertMockTicketData();
}
