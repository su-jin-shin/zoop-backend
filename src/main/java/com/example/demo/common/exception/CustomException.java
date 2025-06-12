package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    abstract public HttpStatus getStatus();
    abstract public String getMessage();
}
