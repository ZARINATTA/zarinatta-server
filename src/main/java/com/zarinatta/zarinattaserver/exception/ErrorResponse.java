package com.zarinatta.zarinattaserver.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String code;
    private String message;
    private Object messageExtra;

    private ErrorResponse(String code, String message, Object messageExtra) {
        this.code = code;
        this.message = message;
        this.messageExtra = messageExtra;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.of(errorCode, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, Object extraInfo) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getDescription(), extraInfo);
    }
}
