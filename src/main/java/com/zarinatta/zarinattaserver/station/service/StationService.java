package com.zarinatta.zarinattaserver.station.service;

import com.zarinatta.zarinattaserver.enums.StationCode;
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

    private final List<String> entireStations = Arrays.stream(StationCode.values())
            .map(Enum::name)
            .toList();

    public List<String> searchKeyword(String keyword) {
        return entireStations.stream().filter(name -> name.contains(keyword)).toList();
    }
}
