package com.example.demo.chat.dto;

import com.example.demo.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChatRoomResponseDto {

    private final Long chatRoomId;
    private final LocalDateTime createdAt;
    private String title;

    public ChatRoomResponseDto(Long chatRoomId, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
    }

    // entity -> dto
    public static ChatRoomResponseDto fromEntity(ChatRoom saved) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(saved.getChatRoomId())
                .createdAt(saved.getCreatedAt())
                .title(saved.getTitle())
                .build();
    }
}
