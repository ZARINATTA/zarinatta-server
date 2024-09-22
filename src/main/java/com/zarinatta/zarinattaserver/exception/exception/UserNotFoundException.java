package com.zarinatta.zarinattaserver.exception.exception;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends ZarinattaException {

    private String detailInfo;

    public UserNotFoundException(String detailInfo) {
        super(ErrorCode.USER_NOT_FOUND);
        this.detailInfo = detailInfo;
    }
}
