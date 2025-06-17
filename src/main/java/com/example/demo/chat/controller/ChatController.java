package com.example.demo.chat.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.chat.dto.ChatRoomDetailResponseDto;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.dto.MessageDto;
import com.example.demo.chat.service.ChatService;
import com.example.demo.chat.type.SenderType;
import com.example.demo.common.response.ResponseResult;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.demo.common.response.SuccessMessage.GET_SUCCESS;

@RestController
@RequestMapping("/chats")
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring DI에서 안전하게 관리됨")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendMessage(@AuthenticationPrincipal LoginUser loginUser, @RequestBody MessageDto requestMessageDto) {
        //Long userId = requestMessageDto.getUserId();
        Long userId = Long.valueOf(loginUser.getUsername());
        Long chatRoomId = requestMessageDto.getChatRoomId();
        SenderType senderType = requestMessageDto.getSenderType();
        String content = requestMessageDto.getContent();

        log.info("chatRoomId: {}, senderType: {}, content: {}", chatRoomId, senderType, content);

        // 1. 채팅방 생성
        if (chatRoomId == null) {
            chatRoomId = chatService.createChatRoom(userId);
            log.info("{}번 채팅방이 생성됨", chatRoomId);
            requestMessageDto.setChatRoomId(chatRoomId);
        }

        // 2. 메시지 저장
        MessageDto responseMessageDto = chatService.saveMessage(requestMessageDto);

        return ResponseEntity.ok(Map.of(
                "chatRoomId", chatRoomId,
                "messageId", responseMessageDto.getMessageId(),
                "createdAt", responseMessageDto.getCreatedAt()
        ));
    }

    // 채팅방 제목 수정
    @PatchMapping("/{chatRoomId}")
    public ResponseEntity<Void> updateChatRoomTitle(@PathVariable Long chatRoomId, @RequestBody ChatRoomDto chatRoomDto) {
        String title = chatRoomDto.getTitle();
        chatService.updateChatRoomTitle(chatRoomId, chatRoomDto.getTitle());
        log.info("{}번 채팅방의 제목이 변경됨: {}", chatRoomId, title);
        return ResponseEntity.noContent().build();
    }

    // 채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatService.deleteChatRoom(chatRoomId);
        log.info("{}번 채팅방이 삭제됨", chatRoomId);
        return ResponseEntity.noContent().build();
    }

    // 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = Long.valueOf(loginUser.getUsername());
        List<ChatRoomDto> chatRooms = chatService.getUserChatRooms(userId);
        log.info("[userId: {}]의 채팅방 목록 조회 - {}개", userId, chatRooms.size());
        return ResponseEntity.ok(chatRooms);
    }

    // 채팅 상세보기
    @GetMapping("/detail/{chatRoomId}")
    public ResponseEntity<?> getChatRoomsDetails(@AuthenticationPrincipal LoginUser loginUser,
                                                 @PathVariable("chatRoomId") Long chatRoomId) {
        Long userId = Long.valueOf(loginUser.getUsername());
        ChatRoomDetailResponseDto responseDto = chatService.getUserChatRoomMessages(userId, chatRoomId);
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        responseDto
                )
        );
    }
}
