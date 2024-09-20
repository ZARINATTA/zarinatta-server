package com.zarinatta.zarinattaserver.exception.exception;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookMarkNotFoundException extends ZarinattaException {

    private String detailInfo;

    public BookMarkNotFoundException(String detailInfo) {
        super(ErrorCode.BOOKMARK_NOT_FOUND);
        this.detailInfo = detailInfo;
    }
}
