package com.zarinatta.zarinattaserver.exception.exception.NotPermit;

import com.zarinatta.zarinattaserver.exception.ErrorCode;

public class BookMarkPermitException extends NotPermitException {

    public BookMarkPermitException(String method, String detailMessage) {
        super(ErrorCode.BOOKMARK_PERMISSION_DENIED, method, detailMessage);
    }
}
