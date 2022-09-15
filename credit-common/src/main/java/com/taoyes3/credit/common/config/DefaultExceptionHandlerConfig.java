package com.taoyes3.credit.common.config;

import com.taoyes3.credit.common.exception.CreditBindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author taoyes3
 * @date 2022/9/15 12:15
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandlerConfig {
    @ExceptionHandler(CreditBindException.class)
    public ResponseEntity<String> creditBindExceptionHandler(CreditBindException e) {
        log.error("creditBindException Message:{}", e.getMessage());
        return ResponseEntity.status(e.getHttpStatusCode()).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException Message:{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }
}
