package com.zarinatta.zarinattaserver.exception.exception.NotFound;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookMarkNotFoundException extends NotFoundException {

    public Long userId;

    public BookMarkNotFoundException(String method) {
        super(ErrorCode.BOOKMARK_NOT_FOUND, method);
    }

    public BookMarkNotFoundException(String method, Long userId) {
        super(ErrorCode.BOOKMARK_NOT_FOUND, method);
        this.userId = userId;
    }
}
