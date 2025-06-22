package com.example.demo.chat.dto;

import com.example.demo.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomRequestDto {

    private Long chatRoomId;      // 채팅방 번호
    private String title;         // 채팅방 제목
    private String lastMatchingMessage;     // 검색어와 동일한 채팅내용 중에 가장 최신
    private LocalDateTime lastMessageAt;    // 마지막 메세지 시간

    // Entity -> Dto 변환 // 검색이 없을 때 로그인한 유저의 전체 채팅방 조회
    public static ChatRoomRequestDto fromEntity(ChatRoom chatRoom) {
        return fromEntity(chatRoom, null); // 검색어 없는 경우
    }

    // Entity -> Dto 변환 // 검색이 있을 때 채팅방 조회
    public static ChatRoomRequestDto fromEntity(ChatRoom chatRoom, String lastMatchingMessage) {
        return ChatRoomRequestDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .title(chatRoom.getTitle())
                .lastMatchingMessage(lastMatchingMessage)
                .lastMessageAt(chatRoom.getLastMessageAt())
                .build();
    }
}
