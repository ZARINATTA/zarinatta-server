package com.zarinatta.zarinattaserver.entity;

import com.zarinatta.zarinattaserver.enums.StationSection;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "STATION")
@Table(name = "STATION")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @Column(name = "STATION_SECTION")
    @Enumerated(EnumType.STRING)
    private StationSection section;

    @Column(name = "STATION_NAME")
    private String name;

    @Column(name = "STATION_COUNT")
    @Builder.Default
    private long count = 0;
}
