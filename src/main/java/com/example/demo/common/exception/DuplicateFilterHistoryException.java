package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.DUPLICATED_KEYWORD_FILTER_HISTORY;

public class DuplicateFilterHistoryException extends CustomException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return DUPLICATED_KEYWORD_FILTER_HISTORY.getMessage();
    }
}
