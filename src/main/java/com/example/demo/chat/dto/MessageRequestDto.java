package com.example.demo.chat.dto;

import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.chat.type.SenderType;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequestDto {

    @Setter
    private Long chatRoomId;
    private String title;
    private String content;
    private SenderType senderType;
    private FilterRequestDto filterRequestDto;

    public MessageRequestDto(Long chatRoomId, String content, SenderType senderType) {
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.senderType = senderType;
    }

    public void applyUserMessage() {
        this.senderType = SenderType.USER;
    }

    public void applyAiReply(String aiReply, SenderType senderType) {
        this.content = aiReply;
        this.senderType = senderType;
    }

}
