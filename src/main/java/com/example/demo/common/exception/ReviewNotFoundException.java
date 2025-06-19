package com.example.demo.common.exception;

import com.example.demo.common.response.FailedMessage;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends CustomException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return FailedMessage.REVIEW_NOT_FOUND.getMessage(); // or "이미 삭제된 리뷰입니다."
    }
}
