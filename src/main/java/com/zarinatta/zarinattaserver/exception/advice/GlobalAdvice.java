package com.zarinatta.zarinattaserver.exception.advice;

import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.exception.ErrorResponse;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {
    //== Natural Exception Handler ==//
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity BindExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, List<String>> messageExtra = new HashMap<>(); //ex) type: [에러1, 에러2, 에러3...]
        bindingResultToMap(e.getBindingResult(), messageExtra);
        return ResponseEntity
                .status(BAD_REQUEST).body(ErrorResponse.of(ErrorCode.INVALID_REQUEST_ERROR, messageExtra));
    }

    private void bindingResultToMap(BindingResult e, Map<String, List<String>> messageExtra) {
        if(e.hasErrors()){
            e.getFieldErrors().forEach(fieldError -> {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                messageExtra.put(fieldName, List.of(errorMessage));
            });
        }
    }


    //== Custom Exception Handler ==//
    @ExceptionHandler(ZarinattaException.class)
    public ResponseEntity ZarinattaExceptionHandler(ZarinattaException e) {
        return ResponseEntity
                .status(e.getExceptionType().getHttpStatus())
                .body(ErrorResponse.of(e.getExceptionType(), null));
    }
}