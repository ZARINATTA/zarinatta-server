package com.zarinatta.zarinattaserver.bookmark.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyBookMarkSearchResponse {
    private Long bookmarkId;
    private Long ticketId;
}
