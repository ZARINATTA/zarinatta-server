package com.zarinatta.zarinattaserver.bookmark.dto.request;

import com.zarinatta.zarinattaserver.enums.BookMarkStatus;

public record BookMarkStatusUpdateRequest(
        Long bookMarkId,
        BookMarkStatus updateStatus
) {
}
