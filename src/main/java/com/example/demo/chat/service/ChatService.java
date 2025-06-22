package com.example.demo.chat.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.*;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserInfoRepository userInfoRepository;

    //private int CHATBOT_MESSAGE_ORDER = 0;



//    // 채팅방 생성
//    @Transactional
//    public Long createChatRoom(UserInfo userInfo, String title) {
//
//            // 채팅방 생성
//            ChatRoom chatRoom = new ChatRoom(userInfo);
//            chatRoom.setTitle(title);
//            ChatRoom saved = chatRoomRepository.save(chatRoom);
//
//            return saved.getChatRoomId();
//    }
//
//    // 채팅방 찾기
//    private ChatRoom findByChatRoomId(Long chatRoomId, String context) {
//        return chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId, context));
//    }
//
//    // 메시지 저장
//    @Transactional
//    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto) {
//        try {
//            ChatRoom chatRoom = findByChatRoomId(messageRequestDto.getChatRoomId(), ErrorMessages.CHAT_SAVE_MESSAGE_FAILED); // 채팅방의 존재 여부를 확인하여, 없으면 예외 발생 (EntityNotFoundException)
//            Message message = new Message(chatRoom, messageRequestDto.getSenderType(), messageRequestDto.getContent());
//            Message saved =  messageRepository.save(message);
//            chatRoom.updateLastMessageAt(saved.getCreatedAt()); // 채팅방의 마지막 메시지 발송 시각을 갱신
//            return new MessageResponseDto(messageRequestDto.getChatRoomId(), saved.getMessageId(), saved.getCreatedAt());
//        } catch (Exception e) {
//            throw new ChatServiceException(ErrorMessages.CHAT_SAVE_MESSAGE_FAILED, messageRequestDto.getChatRoomId(), e);
//        }
//    }
//
//    // 메시지 저장
//    @Transactional
//    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto, List<PropertyDto> properties) {
//        try {
//            ChatRoom chatRoom = findByChatRoomId(messageRequestDto.getChatRoomId(), ErrorMessages.CHAT_SAVE_MESSAGE_FAILED); // 채팅방의 존재 여부를 확인하여, 없으면 예외 발생 (EntityNotFoundException)
//            Message message = new Message(chatRoom, messageRequestDto.getSenderType(), messageRequestDto.getContent(), properties);
//            Message saved =  messageRepository.save(message);
//            chatRoom.updateLastMessageAt(saved.getCreatedAt()); // 채팅방의 마지막 메시지 발송 시각을 갱신
//            return new MessageResponseDto(messageRequestDto.getChatRoomId(), saved.getMessageId(), saved.getCreatedAt());
//        } catch (Exception e) {
//            throw new ChatServiceException(ErrorMessages.CHAT_SAVE_MESSAGE_FAILED, messageRequestDto.getChatRoomId(), e);
//        }
//    }
//
//    // 메시지 테이블에 추천 리스트 update
//    @Transactional
//    public void updateMessage(Long messageId, List<PropertyDto> properties) {
//        Message message = messageRepository.findById(messageId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 메시지를 찾을 수 없습니다. ID: " + messageId));
//        message.updateProperties(properties);
//    }
//
//    // 채팅방 제목 변경
//    @Transactional
//    public void updateChatRoomTitle(Long chatRoomId, String title) {
//        try {
//            ChatRoom chatRoom = findByChatRoomId(chatRoomId, ErrorMessages.CHAT_UPDATE_TITLE_FAILED);
//            chatRoom.updateTitle(title);
//        } catch (Exception e) {
//            throw new ChatServiceException(ErrorMessages.CHAT_UPDATE_TITLE_FAILED, chatRoomId, e);
//        }
//    }
//
//    // 채팅방 삭제 (소프트 삭제)
//    @Transactional
//    public void deleteChatRoom(Long chatRoomId) {
//        try {
//            findByChatRoomId(chatRoomId, ErrorMessages.CHAT_DELETE_FAILED);
//            chatRoomRepository.softDeleteChatRoom(chatRoomId, LocalDateTime.now());
//            messageRepository.softDeleteMessages(chatRoomId, LocalDateTime.now()); // 메시지도 소프트 삭제
//        } catch (Exception e) {
//            throw new ChatServiceException(ErrorMessages.CHAT_DELETE_FAILED, chatRoomId, e);
//        }
//    }
//
    // 채팅방 목록 조회
    public List<ChatRoomRequestDto> getAllChatRooms(Long userId) {
        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        // 로그인한 유저의 모든 채팅방 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserInfoAndIsDeletedFalseOrderByLastMessageAtDesc(userInfo);

        return chatRooms.stream()
                .map(ChatRoomRequestDto::fromEntity) // 검색어 없으니 null 전달되는 버전 사용
                .toList();
    }

//    // 로그인한 사용자가 특정 채팅방 선택시 채팅내용 조회
//    @Transactional(readOnly = true)
//    public ChatRoomDetailResponseDto getUserChatRoomMessages(Long userId, Long chatRoomId) {
//
//        // 사용자 조회
//        UserInfo userInfo = userInfoRepository.findByUserId(userId)
//                .orElseThrow(UserNotFoundException::new);
//
//        // 용자의 채팅방인지 확인 후 조회
//        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUserInfo(chatRoomId, userInfo);
//        if (chatRoom == null) {
//            throw new NotFoundException();
//        }
//
//        // 로그인한 유저가 특정 채팅방을 조회할 때 삭제되지 않은 메세지만 조회
//        List<Message> messages = messageRepository.findByChatRoom_ChatRoomIdAndDeletedAtIsNull(chatRoomId);
//
//        // 메시지 DTO로 변환
//        List<ChatRoomDetailResponseDto.MessageResponseDto> messageDtos = messages.stream()
//                .map(ChatRoomDetailResponseDto.MessageResponseDto::from)
//                .toList();
//
//        // 응답 DTO 생성
//        return ChatRoomDetailResponseDto.from(chatRoom, messageDtos);
//    }

    // 채팅방 검색( 제목 및 채팅내용으로 검색)
    @Transactional(readOnly = true)
    public List<ChatRoomRequestDto> searchChatRooms(Long userId, String searchText) {

        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        // 삭제되지 않은 채팅방 전체 조회 (생성일 내림차순 정렬)
        return chatRoomRepository.findByUserInfoAndIsDeletedFalseOrderByCreatedAtDesc(userInfo).stream()
                .map(chatRoom -> {
                    String title = chatRoom.getTitle();
                    boolean matchesTitle = title.contains(searchText);

                    // 사용자가 검색한 검색어와 매칭되는 채팅방 내용중에서 가장 최신의 데이터 담기
                    Optional<Message> lastMatchingMessage = messageRepository
                            .findTopByChatRoomAndContentContainingIgnoreCaseOrderByMessageIdDesc(chatRoom, searchText);

                    if (matchesTitle || lastMatchingMessage.isPresent()) {
                        String messageContent = lastMatchingMessage
                                .map(Message::getContent)
                                .orElse(null);
                        return ChatRoomRequestDto.fromEntity(chatRoom, messageContent);

                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

//    // 특정 채팅방의 가장 최근 메시지(가장 최근 챗봇 응답) 조회
//    public MessageDto getRecentMessage(Long chatRoomId) {
//        Message m = messageRepository.findTop1ByChatRoom_ChatRoomIdAndSenderTypeOrderByCreatedAtDesc(chatRoomId, SenderType.CHATBOT);
//
//        log.info("m: {}", m);
//        return MessageDto.builder()
//                .chatRoomId(chatRoomId)
//                .messageId(m.getMessageId())
//                .senderType(m.getSenderType())
//                .content(m.getContent())
//                .properties(m.getProperties())
//                .createdAt(m.getCreatedAt())
//                .build();
//    }
//
//    @Async
//    public void generateAndSaveAiResponse(MessageRequestDto request) {
//        //String userMessageContent = request.getContent();
//        //String aiReply = customAI.generateReply(userMessageContent); // AI 호출
//
//        String aiReply = ++CHATBOT_MESSAGE_ORDER + ". ai의 답변입니다.";
//        request.applyAiReply(aiReply, SenderType.CHATBOT);
//
//        MessageResponseDto aiMessage = saveMessage(request);
//        log.info("ai의 답변 DB 저장 완료: {}", aiMessage);
//    }
//
//    @Async
//    public void generateAndSaveAiResponse(MessageRequestDto request, List<PropertyDto> properties) {
//        //String userMessageContent = request.getContent();
//        //String aiReply = customAI.generateReply(userMessageContent); // AI 호출
//
//        String aiReply;
//        if (properties == null) {
//            aiReply = "매물 추천에 실패하였습니다. 다시 한번 시도해 주세요.";
//        } else if (properties.isEmpty()) {
//            aiReply = "해당 조건에 맞는 매물이 존재하지 않습니다. 필터를 다시 설정해 주세요.";
//        } else {
//            aiReply = "매물을 추천해 드리겠습니다.";
//        }
//        request.applyAiReply(aiReply, SenderType.CHATBOT);
//
//        MessageResponseDto aiMessage = saveMessage(request, properties);
//        log.info("ai의 답변 DB 저장 완료: {}", aiMessage);
//    }
//
//
}