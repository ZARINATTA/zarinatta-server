package com.zarinatta.zarinattaserver.exception.advice;

import com.zarinatta.zarinattaserver.exception.ErrorResponse;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(ZarinattaException.class)
    public ResponseEntity ZarinattaExceptionHandler(ZarinattaException e) {
        return ResponseEntity
                .status(e.getExceptionType().getHttpStatus())
                .body(ErrorResponse.of(e.getExceptionType(), null));
    }
}