package com.example.demo.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MessageResponseDto {

    private final Long chatRoomId;
    private final Long messageId;
    private final LocalDateTime createdAt;

}
