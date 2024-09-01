package com.zarinatta.zarinattaserver.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zarinatta.zarinattaserver.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {
}
