package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import static com.example.demo.common.response.FailedMessage.PROPERTY_NOT_FOUND;
import static com.example.demo.common.response.FailedMessage.RESOURCE_NOT_FOUND;

public class PropertyNotFoundException extends CustomException{

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return PROPERTY_NOT_FOUND.getMessage();
    }

}
