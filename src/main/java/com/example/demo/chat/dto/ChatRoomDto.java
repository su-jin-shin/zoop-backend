package com.example.demo.chat.dto;

import com.example.demo.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomDto {

    private int order;
    private Long chatRoomId;
    private String title;
    private LocalDateTime lastMessageAt;

    // Entity -> Dto 변환
    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .title(chatRoom.getTitle())
                .lastMessageAt(chatRoom.getLastMessageAt())
                .build();
    }
}
