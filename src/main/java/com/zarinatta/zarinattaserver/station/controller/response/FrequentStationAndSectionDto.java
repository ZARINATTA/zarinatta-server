package com.zarinatta.zarinattaserver.station.controller.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FrequentStationAndSectionDto(
        List<FrequentStationDto> stations,
        List<String> sections
) {
}