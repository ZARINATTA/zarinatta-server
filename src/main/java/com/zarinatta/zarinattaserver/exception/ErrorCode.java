package com.zarinatta.zarinattaserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400
    INVALID_REQUEST_ERROR("G-003", HttpStatus.BAD_REQUEST, "INVALID_REQUEST"),

    // 401
    INVALID_TOKEN_ERROR("G-001", HttpStatus.UNAUTHORIZED, "TOKEN_INVALID"),
    EXPIRED_TOKEN_ERROR("G-002", HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED"),

    // 404
    USER_NOT_FOUND("U-001", HttpStatus.NOT_FOUND, "USER_NOT_FOUND"),
    // 409
    EXIST_USER("U-002", HttpStatus.CONFLICT, "EXIST_USER"),

    // 500
    KAKAO_SERVER_ERROR("K-001", HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO_API_RESPONSE_ERROR"),
    AUTH_SERVER_ERROR("A-S-001", HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_API_SERVER_ERROR");

    private final String code;
    private final HttpStatus httpStatus;
    private final String description;

    ErrorCode(String code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
