package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.RESOURCE_NOT_FOUND;

// 공통 404 에러 메세지
public class NotFoundException extends CustomException{
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return RESOURCE_NOT_FOUND.getMessage();
    }
}
