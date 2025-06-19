package com.example.demo.common.exception;

import com.example.demo.common.response.FailedMessage;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.COMMENT_NOT_FOUND;


public class CommentNotFoundException extends CustomException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {return COMMENT_NOT_FOUND.getMessage();}

}