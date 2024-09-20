package com.zarinatta.zarinattaserver.exception.exception.NotFound;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String method) {
        super(ErrorCode.USER_NOT_FOUND, method);
    }
}
