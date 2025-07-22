package com.zarinatta.zarinattaserver.station.controller.response;

import com.zarinatta.zarinattaserver.enums.StationSection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FrequentStationDto {
    private StationSection section;
    private String name;
}
