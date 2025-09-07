package com.zarinatta.zarinattaserver.station.service;

import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationAndSectionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Test
    void testSearchKeyword() {
        String keyword = "순천";
        var result = stationService.searchKeyword(keyword);
        assertNotNull(result);
        // StationCode 값 중 "SEOUL"이 포함된 값이 있어야 함
        assertTrue(result.stream().anyMatch(name -> name.contains(keyword)));
    }

    @Test
    void testFindStationsByCount() {
        FrequentStationAndSectionDto dto = stationService.findStationsByCount();
        assertNotNull(dto);
        // stations 리스트는 데이터 유무에 관계없이 null이 아니어야 함
        assertNotNull(dto.stations());
        // sections는 StationSection enum의 값들을 포함하므로 비어있지 않아야 함
        assertNotNull(dto.sections());
        assertFalse(dto.sections().isEmpty());
    }

    @Test
    void testUpdateCount() {
        // updateCount 메소드는 업데이트 쿼리를 실행하는데 예외 없이 실행되는지 확인
        var stationNames = stationService.searchKeyword("순천");
        assertDoesNotThrow(() -> stationService.updateCount(stationNames));
    }
}