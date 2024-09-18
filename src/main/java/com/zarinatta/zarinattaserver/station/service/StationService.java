package com.zarinatta.zarinattaserver.station.service;

import com.zarinatta.zarinattaserver.enums.StationCode;
import com.zarinatta.zarinattaserver.enums.StationSection;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationAndSectionDto;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;
import com.zarinatta.zarinattaserver.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    private final List<String> entireStations = Arrays.stream(StationCode.values())
            .map(Enum::name)
            .toList();

    private final List<String> entireStationSections = Arrays.stream(StationSection.values())
            .map(Enum::name)
            .toList();

    public List<String> searchKeyword(String keyword) {
        log.info("[--- keyword: "+keyword+" ----]");
        log.info("[--- list 1nd value: "+entireStations.get(0)+" ---]");
        return entireStations.stream().filter(name -> name.contains(keyword)).toList();
    }

    public FrequentStationAndSectionDto findStationsByCount() {
        List<FrequentStationDto> list = stationRepository.findStationsByCount();
        return FrequentStationAndSectionDto.builder().stations(list).sections(entireStationSections).build();
    }

    public void updateCount(List<String> nameList) {
        for(String name : nameList) {
            log.error("[updateCount] name is not valid station name");
        }

        stationRepository.updateCount(nameList);
    }
}
