package com.zarinatta.zarinattaserver.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyBookMarkRequest {
    @NotNull(message = "만료된 즐겨 찾기 요청 여부 입력 필수")
    private Boolean expire;
    private int page = 1;
    private int size = 10;
}
