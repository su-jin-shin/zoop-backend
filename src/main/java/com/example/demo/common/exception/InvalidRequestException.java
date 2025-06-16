package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.BAD_REQUEST_DEFAULT;

// 공통 400번 에러 메세지
public class InvalidRequestException extends CustomException {


    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return BAD_REQUEST_DEFAULT.getMessage();
    }


}
