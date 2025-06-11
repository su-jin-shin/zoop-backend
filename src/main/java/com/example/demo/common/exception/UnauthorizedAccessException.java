package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.UNAUTHORIZED_ACCESS;

// 공통 403 에러 메세지
public class UnauthorizedAccessException extends CustomException{
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return UNAUTHORIZED_ACCESS.getMessage();
    }
}
