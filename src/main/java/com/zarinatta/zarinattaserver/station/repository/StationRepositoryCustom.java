package com.zarinatta.zarinattaserver.station.repository;

import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;
import com.zarinatta.zarinattaserver.ticket.controller.dto.request.TicketSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StationRepositoryCustom {
    List<FrequentStationDto> findStationsByCount();
    void updateCount(List<String> nameList);
}
