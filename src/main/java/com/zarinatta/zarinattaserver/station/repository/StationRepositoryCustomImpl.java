package com.zarinatta.zarinattaserver.station.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zarinatta.zarinattaserver.entity.QStation;
import com.zarinatta.zarinattaserver.station.controller.response.FrequentStationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zarinatta.zarinattaserver.entity.QStation.station;


@Repository
@RequiredArgsConstructor
public class StationRepositoryCustomImpl implements StationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QStation qStation = station;

    @Override
    public List<FrequentStationDto> findStationsByCount() {
        return queryFactory.select(Projections.fields(
                        FrequentStationDto.class,
                        qStation.name.as("name"),   // DTO 필드명과 엔티티 필드명이 같을 때
                        qStation.section.as("section")
                ))
                .from(qStation)
                .orderBy(qStation.count.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public void updateCount(List<String> nameList) {
        queryFactory
                .update(qStation)
                .set(qStation.count, qStation.count.add(1))
                .where(qStation.name.in(nameList))
                .execute();
    }
}
