package com.example.demo.chat.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.chat.dto.*;
import com.example.demo.chat.service.ChatService;
import com.example.demo.chat.type.SenderType;
import com.example.demo.chat.util.UserFilterSender;
import com.example.demo.common.response.ResponseResult;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.common.response.SuccessMessage.GET_SUCCESS;

@RestController
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring DI에서 안전하게 관리됨")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageRequestDto request) {
        log.info("request: {}", request);

        // TODO: 현재는 임시의 userId 값을 사용하지만, 추후 JWT에서 추출하도록 수정 예정
        Long userId = 2L;
        Long chatRoomId = request.getChatRoomId();
        SenderType senderType = request.getSenderType();
        String content = request.getContent();
        FilterDto filters = request.getFilters();

        log.info("chatRoomId: {}, senderType: {}, content: {}", chatRoomId, senderType, content);

        // 1. 채팅방 생성
        if (chatRoomId == null) {
            chatRoomId = chatService.createChatRoom(userId, request.getTitle());
            log.info("{}번 채팅방이 생성됨", chatRoomId);
            request.setChatRoomId(chatRoomId);
        }

        // 2. 메시지 저장
        MessageResponseDto response = chatService.saveMessage(request);

        // 3. AI 답변 호출
        if (filters == null) {
            chatService.generateAndSaveAiResponse(request);
        } else {
            // 크롤링 로직 시작
            log.info("filters: {}", filters);
            List<PropertyDto> properties = null;
            try {
                properties = UserFilterSender.send(filters);
                chatService.updateMessage(response.getMessageId(), properties);
                log.info("properties: {}", properties);
            } catch(Exception e) {
                log.error("크롤링 실패", e);
            } finally {
                chatService.generateAndSaveAiResponse(request, properties);
            }
        }
        log.info("response: {}", response);
        return ResponseEntity.ok(response);
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
    // 로그인한 사용자의 채팅방 중에서 제목이나 내용으로 조회
    @GetMapping("/search")
    public ResponseEntity<?> searchChatRoom(@AuthenticationPrincipal LoginUser loginUser,
                                            @RequestParam String searchText) {
        //Long userId = Long.valueOf(loginUser.getUsername());
        Long userId = 4L;
        List<ChatRoomSearchDto> chatRooms = chatService.searchChatRooms(userId, searchText);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        chatRooms
                )
        );
    }

    // 특정 채팅방의 가장 최근 챗봇 메시지 조회
    @GetMapping("/{chatRoomId}/recent")
    public ResponseEntity<MessageDto> getRecentChatMessage(@PathVariable Long chatRoomId) {
        MessageDto message = chatService.getRecentMessage(chatRoomId);
        return ResponseEntity.ok(message);
    }

}
