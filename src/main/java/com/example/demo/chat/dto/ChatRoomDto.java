package com.example.demo.chat.dto;

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

}
