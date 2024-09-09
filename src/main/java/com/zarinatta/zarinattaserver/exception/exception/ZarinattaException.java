package com.zarinatta.zarinattaserver.exception.exception;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ZarinattaException extends RuntimeException{

    private ErrorCode exceptionType;

    public ZarinattaException(ErrorCode exceptionType) {
        super(String.format("[%s]: %s", exceptionType.getHttpStatus(), exceptionType.getDescription()));
        this.exceptionType = exceptionType;
    }
}
