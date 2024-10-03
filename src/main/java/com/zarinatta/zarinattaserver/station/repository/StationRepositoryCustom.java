package com.zarinatta.zarinattaserver.station.repository;

import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;

import java.util.List;

public interface StationRepositoryCustom {
    List<FrequentStationDto> findStationsByCount();
    void updateCount(List<String> nameList);
}
