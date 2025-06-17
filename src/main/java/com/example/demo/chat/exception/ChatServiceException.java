package com.example.demo.chat.exception;

public class ChatServiceException extends RuntimeException {
    public ChatServiceException(String message) {
        super(message);
    }

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatServiceException(String CHAT_ERROR_TYPE, Long chatRoomId, Throwable cause) {
        super(String.format("%s. chatRoomId=%d", CHAT_ERROR_TYPE, chatRoomId), cause);
    }
}
