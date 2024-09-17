package com.zarinatta.zarinattaserver.station.controller.response;

import com.zarinatta.zarinattaserver.enums.StationSection;
import lombok.Getter;

@Getter
public class FrequentStationDto {
    private StationSection section;
    private String name;
}
