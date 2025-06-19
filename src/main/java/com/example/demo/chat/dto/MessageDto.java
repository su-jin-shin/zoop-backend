package com.example.demo.chat.dto;

import com.example.demo.chat.type.SenderType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MessageDto {

    private Long chatRoomId;
    private Long messageId;
    private String content;
    private SenderType senderType;
    private List<PropertyDto> properties;
    private LocalDateTime createdAt;

}
