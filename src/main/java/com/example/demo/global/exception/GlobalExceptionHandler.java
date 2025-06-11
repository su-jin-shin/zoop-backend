package com.example.demo.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
//        String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
//                .map(FieldError::getDefaultMessage)
//                .orElse("잘못된 입력입니다.");
//        return ResponseEntity.badRequest().body(new ErrorResponse(message));
//    }
//
//    public record ErrorResponse(String message) {}
//}
