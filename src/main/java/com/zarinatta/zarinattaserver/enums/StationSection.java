package com.zarinatta.zarinattaserver.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StationSection {
    SEOUL_GYEONGGI("서울_경기"),
    DAEJEON_CHUNGCHEONG("대전_충청"),
    GYEONGSANG("경상_부산_대구"),
    GANGWON("강원"),
    JEOLLA("전라_광주");

    private final String koreaName;
}
