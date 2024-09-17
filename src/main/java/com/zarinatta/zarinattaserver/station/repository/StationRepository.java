package com.zarinatta.zarinattaserver.station.repository;

import com.zarinatta.zarinattaserver.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long>, StationRepositoryCustom {
}

