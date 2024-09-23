package com.zarinatta.zarinattaserver.exception.exception.NotFound;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import lombok.Getter;

@Getter
public class NotFoundException extends ZarinattaException {

    private String method;

    public NotFoundException(ErrorCode errorCode, String method) {
        super(errorCode);
        this.method = method;
    }
}
