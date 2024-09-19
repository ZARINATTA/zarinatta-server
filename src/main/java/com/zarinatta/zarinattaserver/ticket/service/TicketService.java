package com.zarinatta.zarinattaserver.ticket.service;

import com.zarinatta.zarinattaserver.station.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zarinatta.zarinattaserver.entity.Ticket;
import com.zarinatta.zarinattaserver.ticket.dto.request.TicketSearchRequest;
import com.zarinatta.zarinattaserver.ticket.dto.response.PageTicketResponse;
import com.zarinatta.zarinattaserver.ticket.dto.response.TicketSearchResponse;
import com.zarinatta.zarinattaserver.ticket.repository.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional // getTicket에 역별 검색 수 세기 위한 update문 추가로 readOnly = true 제외
public class TicketService {

    private final TicketRepository ticketRepository;

    private final StationService stationService;

    public PageTicketResponse getTicket(TicketSearchRequest searchRequest, Pageable pageable) {
        String arriveStation = searchRequest.getArriveStation().name();
        String departStation = searchRequest.getDepartStation().name();

        List<String> stationNameList = List.of(arriveStation, departStation);
        stationService.updateCount(stationNameList);

        Page<Ticket> ticketBySearchDTO = ticketRepository.findTicketBySearchDTO(searchRequest, pageable);

        List<TicketSearchResponse> content = ticketBySearchDTO.getContent().stream()
                .map(TicketSearchResponse::fromEntity)
                .collect(Collectors.toList());

        return PageTicketResponse.builder()
                .responseList(content)
                .page(ticketBySearchDTO.getNumber() + 1)
                .totalDataCount(ticketBySearchDTO.getTotalElements())
                .totalPageCount(ticketBySearchDTO.getTotalPages())
                .build();
    }
}
