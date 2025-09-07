package com.zarinatta.zarinattaserver.station.controller.response;

import com.zarinatta.zarinattaserver.enums.StationSection;

public record FrequentStationDto(
        StationSection section,
        String name
) {
}
