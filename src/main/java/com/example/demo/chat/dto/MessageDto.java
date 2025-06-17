package com.example.demo.chat.dto;

import com.example.demo.chat.type.SenderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long userId;
    @Setter
    private Long chatRoomId;
    private Long messageId;
    private String content;
    private SenderType senderType;
    private LocalDateTime createdAt;

    public MessageDto(Long messageId, String content, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public MessageDto(Long chatRoomId, SenderType senderType, String aiReply) {
        this.chatRoomId = chatRoomId;
        this.senderType = senderType;
        this.content = aiReply;
    }
}
