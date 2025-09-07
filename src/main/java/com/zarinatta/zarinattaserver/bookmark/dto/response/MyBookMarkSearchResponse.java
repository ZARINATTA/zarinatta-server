package com.zarinatta.zarinattaserver.bookmark.dto.response;

import lombok.*;

@Builder
public record MyBookMarkSearchResponse(
        Long bookmarkId,
        Long ticketId
) {
}
