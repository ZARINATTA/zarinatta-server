package com.zarinatta.zarinattaserver.station.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FrequentStationAndSectionDto {
    private List<FrequentStationDto> stations;
    private List<String> sections;
}
