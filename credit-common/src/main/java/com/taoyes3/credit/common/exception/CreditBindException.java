package com.taoyes3.credit.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author taoyes3
 * @date 2022/9/15 11:52
 */
public class CreditBindException extends RuntimeException {
    private Integer httpStatusCode;
    private Object object;

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public CreditBindException(String msg) {
        super(msg);
        this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
    }
}
