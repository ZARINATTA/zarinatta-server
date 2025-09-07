package com.zarinatta.zarinattaserver.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record MyBookMarkRequest(
        @NotNull(message = "만료된 즐겨 찾기 요청 여부 입력 필수")
        Boolean expire,
        int page,
        int size
) {
}