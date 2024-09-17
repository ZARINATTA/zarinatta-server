package com.zarinatta.zarinattaserver.station.controller;

import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;
import com.zarinatta.zarinattaserver.station.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<String>> searchTicket(@RequestParam("keyword") String keyword) {
        return Map.of("stations", stationService.searchKeyword(keyword));
    }

    @GetMapping("/frequent")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<FrequentStationDto>> findStations() {
        return Map.of("stations", stationService.findStationsByCount());
    }
}

