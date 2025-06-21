package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.*;

public class DuplicatedNicknameException extends CustomException {

    @Override //닉네임
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
    @Override
    public String getMessage() {
        return DUPLICATED_NICKNAME.getMessage();
    }
}
