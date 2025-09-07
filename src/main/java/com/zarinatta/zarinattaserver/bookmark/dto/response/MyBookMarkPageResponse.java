package com.zarinatta.zarinattaserver.bookmark.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MyBookMarkPageResponse(
        List<MyBookMarkResponse> responseList,
        int page,
        long totalDataCount,
        int totalPageCount
) {
}