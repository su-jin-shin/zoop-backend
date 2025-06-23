package com.example.demo.chat.controller;

import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.Filter.dto.request.RefinedFilterDto;
import com.example.demo.Filter.service.FilterService;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.chat.dto.*;
import com.example.demo.chat.service.ChatService;
import com.example.demo.chat.type.SenderType;
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
    private final FilterService filterService;
    
    @PostMapping("/new")
    public ResponseEntity<ChatRoomResponseDto> startChat(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = Long.valueOf(loginUser.getUsername());
        ChatRoomResponseDto response = chatService.createChatRoom(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/filters")
    public ResponseEntity<ChatRoomResponseDto> saveChatFilter(@AuthenticationPrincipal LoginUser loginUser, @RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        Long userId = Long.valueOf(loginUser.getUsername());
        // 필터 저장 (아직 미구현!!)
        filterService.saveChatFilter(userId, chatRoomRequestDto);
        // 제목 저장
        chatRoomRequestDto.updateTitle();
        ChatRoomResponseDto response = chatService.updateChatRoomTitle(chatRoomRequestDto);
        System.out.println(chatRoomRequestDto.getFilterRequestDto());
        // 크롤링 로직 시작
        crawlAndRecommendProperties(chatRoomRequestDto.getFilterRequestDto());
        return ResponseEntity.ok(response);
    }

    private void crawlAndRecommendProperties(FilterRequestDto filterRequestDto) {
        // 크롤링 로직 시작
        RefinedFilterDto filters = RefinedFilterDto.of(filterRequestDto);
        log.info("filters: {}", filters);

        List<PropertyExcelDto> recommendedProperties;
        List<PropertyExcelDto> validRecommendedProperties;

        try {
            recommendedProperties = UserFilterSender.send(filters); // ai의 추천 매물 리스트 반환
            log.info("추천 매물 {}개, recommendedProperties: {}", recommendedProperties.size(), recommendedProperties);
        } catch(Exception e) {
            log.error("크롤링 또는 ai의 호출에 실패하였습니다.", e);
        }


//        if (CollectionUtils.isEmpty(recommendedProperties)) {
//            log.info("ai가 추천해준 매물이 없습니다.");
//            chatService.generateAndSaveAiResponse(request, Constants.MessageResultType.FAILURE, null);
//            return;
//        }
//
//        chatService.generateAndSaveAiResponse(request, Constants.MessageResultType.SUCCESS, validRecommendedProperties);


//        if (CollectionUtils.isEmpty(recommendedProperties)) {
//            log.info("ai가 추천해준 매물이 없습니다.");
//            chatService.generateAndSaveAiResponse(request, Constants.MessageResultType.FAILURE, null);
//            return null;
//        }
//
//        int propertyCount = 1;
//        for(RecommendedPropertyDto p : recommendedProperties) {
//
//            try {
//                if (propertyCount > Constants.MAXIMUM_PROPERTY_COUNT) break;
//
//                //articleNo를 통해 매물테이블에 해당 매물이 존재하는지 확인
//                if (p == null || p.getArticleNo() == null) {
//                    log.warn("추천받은 매물의 정보가 존재하지 않습니다.");
//                    continue;
//                }
//
//                String articleNo = p.getArticleNo();
//                Property property = propertyService.findByArticleNo(articleNo);
//
//                Long propertyId;
//                if (property == null) {
//                    log.debug("추천받은 매물의 articleNo({})를 가진 매물이 존재하지 않습니다.", articleNo);
//
//                    // articleNo, tradeType, filters.getRegionCode 정보로 다시 크롤링해서 가져옴
//                    propertyId = UserFilterSender.send(filters, articleNo); //이거 쓰레드로 제어해야 될듯....!!
//                    if (propertyId == null) continue; // 팔린 매물은 null이 반환되는 것 같다.
//
//                    Property newProperty = propertyService.findByArticleNo(articleNo);
//                    if (newProperty == null) continue;
//                    propertyCount++;
//                    validRecommendedProperties.add(p);
//                    if (!CollectionUtils.isEmpty(p.getSummary())) {
//                        // 매물테이블의 ai요약 컬럼 update
//                        propertyService.updateAiMessage(newProperty, p.getSummary());
//                    }
//
//                } else {
//                    propertyCount++;
//                    validRecommendedProperties.add(p);
//                    propertyId = property.getPropertyId();
//                    if (!CollectionUtils.isEmpty(p.getSummary())) {
//                        // 매물테이블의 ai요약 컬럼 update
//                        propertyService.updateAiMessage(property, p.getSummary());
//                    }
//                }
//                // 추천매물테이블에 추천 받은 매물 insert
//                propertyService.saveRecommendedPropertyForMessage(response.getMessageId(), propertyId);
//            } catch (Exception e) {
//                log.error("추천 매물을 가져오는 데에 실패하였습니다.", e);
//                continue;
//            }
//        }
//        chatService.generateAndSaveAiResponse(request, Constants.MessageResultType.SUCCESS, validRecommendedProperties);

    }

//    @PostMapping
//    public ResponseEntity<MessageResponseDto> sendMessage(@AuthenticationPrincipal LoginUser loginUser, @RequestBody MessageRequestDto request) {
//        log.info("request: {}", request);
//
//        Long userId = Long.valueOf(loginUser.getUsername());
//        Long chatRoomId = request.getChatRoomId();
//        SenderType senderType = SenderType.USER;
//        String content = request.getContent();
//
//        log.info("chatRoomId: {}, senderType: {}, content: {}", chatRoomId, senderType, content);
//
//        // 1. 채팅방 생성
//        if (chatRoomId == null) {
//            //에러처리
//            return null;// 고치기!!!!!!!!!!!
//        }
//
//        // 2. 메시지 저장
//        MessageResponseDto response = chatService.saveMessage(request);
//
//        // 3. AI 답변 호출
//        if (filters == null) {
//            chatService.generateAndSaveAiResponse(request);
//        }
//        //log.info("response: {}", response);
//        //return ResponseEntity.ok(response);
//    }


//
//    // 채팅방 제목 수정
//    @PatchMapping("/{chatRoomId}")
//    public ResponseEntity<Void> updateChatRoomTitle(@PathVariable Long chatRoomId, @RequestBody ChatRoomDto chatRoomDto) {
//        String title = chatRoomDto.getTitle();
//        chatService.updateChatRoomTitle(chatRoomId, chatRoomDto.getTitle());
//        log.info("{}번 채팅방의 제목이 변경됨: {}", chatRoomId, title);
//        return ResponseEntity.noContent().build();
//    }
//
//    // 채팅방 삭제
//    @DeleteMapping("/{chatRoomId}")
//    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long chatRoomId) {
//        chatService.deleteChatRoom(chatRoomId);
//        log.info("{}번 채팅방이 삭제됨", chatRoomId);
//        return ResponseEntity.noContent().build();
//    }
//
//    // 채팅방 목록 조회
//    @GetMapping
//    public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@AuthenticationPrincipal LoginUser loginUser) {
//        Long userId = Long.valueOf(loginUser.getUsername());
//        List<ChatRoomDto> chatRooms = chatService.getUserChatRooms(userId);
//        log.info("[userId: {}]의 채팅방 목록 조회 - {}개", userId, chatRooms.size());
//        return ResponseEntity.ok(chatRooms);
//    }
//
//    // 채팅 상세보기
//    @GetMapping("/detail/{chatRoomId}")
//    public ResponseEntity<?> getChatRoomsDetails(@AuthenticationPrincipal LoginUser loginUser,
//                                                 @PathVariable("chatRoomId") Long chatRoomId) {
//        Long userId = Long.valueOf(loginUser.getUsername());
//        ChatRoomDetailResponseDto responseDto = chatService.getUserChatRoomMessages(userId, chatRoomId);
//        return ResponseEntity.ok(
//                ResponseResult.success(
//                        HttpStatus.OK,
//                        GET_SUCCESS.getMessage(),
//                        responseDto
//                )
//        );
//    }


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
//
//    // 특정 채팅방의 가장 최근 챗봇 메시지 조회
//    @GetMapping("/{chatRoomId}/recent")
//    public ResponseEntity<MessageDto> getRecentChatMessage(@PathVariable Long chatRoomId) {
//        MessageDto message = chatService.getRecentMessage(chatRoomId);
//        return ResponseEntity.ok(message);
//    }
//
}
