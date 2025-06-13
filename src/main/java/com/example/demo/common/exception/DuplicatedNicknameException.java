package com.example.demo.common.exception;

import static com.example.demo.common.response.FailedMessage.*;

public class DuplicatedNicknameException extends InvalidRequestException {

    @Override
    public String getMessage() {
        return DUPLICATED_NICKNAME.getMessage();
    }
}

