package com.zarinatta.zarinattaserver.exception.exception.NotPermit;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import lombok.Getter;

@Getter
public class NotPermitException extends ZarinattaException {

    private String method;
    private String detailMessage;

    public NotPermitException(ErrorCode errorCode, String method, String detailMessage) {
        super(errorCode);
        this.method = method;
        this.detailMessage = detailMessage;
    }
}
