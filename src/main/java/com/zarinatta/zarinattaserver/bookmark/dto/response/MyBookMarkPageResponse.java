package com.zarinatta.zarinattaserver.bookmark.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MyBookMarkPageResponse {
    private List<MyBookMarkResponse> responseList;
    private int page;
    private long totalDataCount;
    private int totalPageCount;

    @Builder
    public MyBookMarkPageResponse(List<MyBookMarkResponse> responseList, int page, long totalDataCount, int totalPageCount) {
        this.responseList = responseList;
        this.page = page;
        this.totalDataCount = totalDataCount;
        this.totalPageCount = totalPageCount;
    }
}
