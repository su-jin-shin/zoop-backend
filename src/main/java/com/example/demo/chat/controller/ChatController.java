package com.example.demo.chat.controller;

import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.Filter.dto.request.RefinedFilterDto;
import com.example.demo.Filter.service.FilterService;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.chat.constants.Constants;
import com.example.demo.chat.dto.*;
import com.example.demo.chat.service.ChatService;
import com.example.demo.chat.service.ChatUpdateService;
import com.example.demo.chat.util.UserFilterSender;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.response.ResponseResult;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
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
    private final ChatUpdateService chatUpdateService;
    private final FilterService filterService;

    // 채팅방 생성
    @PostMapping("/new")
    public ResponseEntity<ChatRoomResponseDto> startChat(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = Long.valueOf(loginUser.getUsername());
        ChatRoomResponseDto response = chatService.createChatRoom(userId);
        return ResponseEntity.ok(response);
    }

    // 채팅 관련 필터 및 채팅 필터 히스토리 저장 후 필터에 맞춰 크롤링
    @PostMapping("/filters/{chatRoomId}")
    public ResponseEntity<ChatRoomResponseDto> saveChatFilter(@AuthenticationPrincipal LoginUser loginUser,
                                                              @RequestBody FilterRequestDto filterRequestDto,
                                                              @PathVariable Long chatRoomId) {
        if(loginUser == null){
            throw new UserNotFoundException();
        }

        Long userId = Long.valueOf(loginUser.getUsername());
        // 필터 저장
        filterService.saveChatFilter(filterRequestDto, chatRoomId);
        // 제목 저장
        ChatRoomResponseDto response = chatService.updateTitle(chatRoomId, filterRequestDto);
        // 크롤링 로직 시작
        crawlAndRecommendProperties(userId, chatRoomId, filterRequestDto);
        return ResponseEntity.ok(response);
    }

    private void crawlAndRecommendProperties(Long userId, Long chatRoomId, FilterRequestDto filterRequestDto) {

        // 크롤링 로직 시작
        RefinedFilterDto filters = RefinedFilterDto.of(filterRequestDto);
        log.info("filters: {}", filters);

        List<PropertyExcelDto> recommendedProperties;

        MessageRequestDto request = new MessageRequestDto();
        request.setChatRoomId(chatRoomId);

        try {
            recommendedProperties = UserFilterSender.send(filters); // ai의 추천 매물 리스트 반환
            log.info("추천 매물 {}개, recommendedProperties: {}", recommendedProperties.size(), recommendedProperties);
        } catch(Exception e) {
            log.error("크롤링 또는 ai의 호출에 실패하였습니다.", e);
            chatService.generateAndSaveAiResponse(userId, request, Constants.MessageResultType.FAILURE, null);
            return;
        }

        if (CollectionUtils.isEmpty(recommendedProperties)) {
            log.info("ai가 추천해준 매물이 없습니다.");
            chatService.generateAndSaveAiResponse(userId, request, Constants.MessageResultType.FAILURE, null);
            return;
        }

        // 이 중 10개만 보내기!! //얘가 지금 db저장이 된 것은 아님. 근데 일단 message테이블의 properties필드에 저장하면 될것같다.!!!!
        // 10개만 반환
        int limit = Math.min(recommendedProperties.size(), Constants.MAXIMUM_PROPERTY_COUNT);
        List<PropertyExcelDto> validRecommendedProperties = new ArrayList<>(recommendedProperties.subList(0, limit));
        log.info("정제된 추천 매물 {}개", validRecommendedProperties.size());
        //chatService.updateMessage(messageId, validRecommendedProperties);
        chatService.generateAndSaveAiResponse(userId, request, Constants.MessageResultType.SUCCESS, validRecommendedProperties);

    }

//    @PostMapping
//    public ResponseEntity<MessageResponseDto> sendMessage(@AuthenticationPrincipal LoginUser loginUser, @RequestBody MessageRequestDto request) {
//        log.info("request: {}", request);
//        Long chatRoomId = request.getChatRoomId();
//        String content = request.getContent();
//
//        if (chatRoomId == null) {
//            throw new IllegalStateException("메시지를 저장할 수 없습니다.");
//        }
//
//        log.info("chatRoomId: {}, content: {}", chatRoomId, content);
//        request.applyUserMessage();
//
//        // 메시지 저장
//        MessageResponseDto response = chatService.saveMessage(request);
//        log.info("response: {}", response);
//        return ResponseEntity.ok(response);
//    }

    // 채팅방 제목 수정
    @PatchMapping("/{chatRoomId}")
    public ResponseEntity<Void> updateChatRoomTitle(@PathVariable Long chatRoomId, @RequestBody ChatRoomRequestDto chatRoomDto) {
        //로그인 처리 해야함
        String title = chatRoomDto.getTitle();
        chatService.updateChatRoomTitle(chatRoomId, chatRoomDto.getTitle());
        log.info("{}번 채팅방의 제목이 변경됨: {}", chatRoomId, title);
        return ResponseEntity.noContent().build();
    }

    // 채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
        //로그인 처리 해야함
        chatService.deleteChatRoom(chatRoomId);
        log.info("{}번 채팅방이 삭제됨", chatRoomId);
        return ResponseEntity.noContent().build();
    }

    // 로그인한 사용자의 채팅방 중에서 제목이나 내용으로 조회
    @GetMapping
    public ResponseEntity<?> searchChatRoom(@AuthenticationPrincipal LoginUser loginUser,
                                            @RequestParam(required = false) String searchText) {
        Long userId = Long.valueOf(loginUser.getUsername());
        List<ChatRoomRequestDto> chatRooms;

        if (searchText == null || searchText.trim().isEmpty()) {
            chatRooms = chatService.getAllChatRooms(userId); // 전체 채팅방 조회

        } else {
            chatRooms = chatService.searchChatRooms(userId, searchText); // 검색한 결과 조회
        }
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        chatRooms
                )
        );
    }

    // 채팅 상세보기
    @GetMapping("/{chatRoomId}")
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

    @GetMapping("/{chatRoomId}/recent")
    public DeferredResult<List<MessageDto>> getChatUpdates(@PathVariable Long chatRoomId) {
        log.info("[LongPoll] chatRoomId={} - 연결 시도", chatRoomId);
        DeferredResult<List<MessageDto>> result = new DeferredResult<>(20000L); // 20초 동안 대기

        result.onCompletion(() ->
                log.info("[LongPoll] chatRoomId={} - 응답 완료", chatRoomId)
        );
        result.onTimeout(() ->
                log.info("[LongPoll] chatRoomId={} - 타임아웃", chatRoomId)
        );
        result.onError((Throwable t) ->
                log.info("[LongPoll] chatRoomId={} - 에러: {}", chatRoomId, t.getMessage())
        );

        chatUpdateService.register(chatRoomId, result);
        return result;
    }

}
