package com.zarinatta.zarinattaserver.ticket.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageTicketResponse(
        List<TicketSearchResponse> responseList,
        int page,
        long totalDataCount,
        int totalPageCount
) {
}