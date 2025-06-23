//package com.example.demo.chat.dto;
//
//
//import com.example.demo.chat.domain.ChatRoom;
//import com.example.demo.chat.domain.Message;
//import com.example.demo.chat.type.SenderType;
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@SuppressFBWarnings(value = "EI_EXPOSE_REP")
//public class ChatRoomDetailResponseDto {
//    private Long chatRoomId;
//    private String title;
//    private List<MessageResponseDto> messages;
//
//    public static ChatRoomDetailResponseDto from(ChatRoom chatRoom, List<MessageResponseDto> messages) {
//        return ChatRoomDetailResponseDto.builder()
//                .chatRoomId(chatRoom.getChatRoomId())
//                .title(chatRoom.getTitle())
//                .messages(messages)
//                .build();
//    }
//
//    @Getter
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @SuppressFBWarnings(value = "EI_EXPOSE_REP")
//    public static class MessageResponseDto {
//        private Long messageId;
//        private SenderType senderType;
//        private String content;
//        private LocalDateTime createdAt;
//
//        public static MessageResponseDto from(Message message) {
//            return MessageResponseDto.builder()
//                    .messageId(message.getMessageId())
//                    .senderType(message.getSenderType())
//                    .content(message.getContent())
//                    .createdAt(message.getCreatedAt())
//                    .build();
//        }
//    }
//}
