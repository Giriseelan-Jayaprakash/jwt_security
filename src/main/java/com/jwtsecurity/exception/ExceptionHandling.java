package com.jwtsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionHandling extends RuntimeException{
    public ExceptionHandling(String message) {
        super(message);
    }
}
