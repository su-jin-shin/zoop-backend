package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.USER_NOT_FOUND;

// 공통 401 에러 메세지
public class UserNotFoundException extends CustomException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return USER_NOT_FOUND.getMessage();
    }
}
