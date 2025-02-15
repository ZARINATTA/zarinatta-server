package com.zarinatta.zarinattaserver.bookmark.dto.request;

import com.zarinatta.zarinattaserver.enums.BookMarkStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookMarkStatusUpdateRequest {
    private Long bookMarkId;
    private BookMarkStatus updateStatus;
}
