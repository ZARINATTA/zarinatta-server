package com.zarinatta.zarinattaserver.exception.exception.NotFound;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TicketNotFoundException extends NotFoundException {

    public TicketNotFoundException(String method) {
        super(ErrorCode.TICKET_NOT_FOUND, method);
    }
}
