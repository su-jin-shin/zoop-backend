//package com.example.demo.chat.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Map;
//
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ChatRoomNotFoundException.class)
//    public ResponseEntity<?> handleChatRoomNotFound(ChatRoomNotFoundException e) {
//        log.error(e.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(Map.of("message", e.getMessage()));
//    }
//
//    @ExceptionHandler(ChatServiceException.class)
//    public ResponseEntity<?> handleChatOperationFailed(ChatServiceException e) {
//        log.error(e.getMessage(), e);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Map.of("message", e.getMessage()));
//    }
//
//}
