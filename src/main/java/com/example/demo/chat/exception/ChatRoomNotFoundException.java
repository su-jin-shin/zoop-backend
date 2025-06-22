package com.example.demo.chat.exception;

import jakarta.persistence.EntityNotFoundException;

public class ChatRoomNotFoundException extends EntityNotFoundException {
    public ChatRoomNotFoundException(String message) {
        super(message);
    }

    public ChatRoomNotFoundException(Long chatRoomId, String context) {
        super(String.format("[%s] 존재하지 않는 채팅방입니다. chatRoomId=%d", context, chatRoomId));
    }
}
