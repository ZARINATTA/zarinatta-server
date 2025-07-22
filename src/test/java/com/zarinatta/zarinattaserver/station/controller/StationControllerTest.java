package com.zarinatta.zarinattaserver.station.controller;

import com.zarinatta.zarinattaserver.enums.StationSection;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationAndSectionDto;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;
import com.zarinatta.zarinattaserver.station.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @Test
    public void searchTicket() throws Exception {
        // given
        String keyword = "서울";
        List<String> stations = Arrays.asList("서울역", "동서울역");
        when(stationService.searchKeyword(keyword)).thenReturn(stations);

        // when & then
        mockMvc.perform(get("/api/v1/station/search")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations[0]").value("서울역"))
                .andExpect(jsonPath("$.stations[1]").value("동서울역"));
    }

    @Test
    public void findStations() throws Exception {
        // ReflectionTestUtils를 사용하여 빈 생성 후 필드 주입
        FrequentStationDto station1 = new FrequentStationDto();
        ReflectionTestUtils.setField(station1, "section", StationSection.서울_경기);
        ReflectionTestUtils.setField(station1, "name", "서울역");

        FrequentStationDto station2 = new FrequentStationDto();
        ReflectionTestUtils.setField(station2, "section", StationSection.경상_부산_대구);
        ReflectionTestUtils.setField(station2, "name", "부산역");

        List<FrequentStationDto> frequentStations = Arrays.asList(station1, station2);
        List<String> sections = Arrays.asList("A", "B");

        FrequentStationAndSectionDto dto = FrequentStationAndSectionDto.builder()
                .stations(frequentStations)
                .sections(sections)
                .build();

        when(stationService.findStationsByCount()).thenReturn(dto);

        mockMvc.perform(get("/api/v1/station/frequent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations[0].name").value("서울역"))
                .andExpect(jsonPath("$.stations[0].section").value("서울_경기"))
                .andExpect(jsonPath("$.stations[1].name").value("부산역"))
                .andExpect(jsonPath("$.stations[1].section").value("경상_부산_대구"))
                .andExpect(jsonPath("$.sections").isArray())
                .andExpect(jsonPath("$.sections[0]").value("A"))
                .andExpect(jsonPath("$.sections[1]").value("B"));
    }
}