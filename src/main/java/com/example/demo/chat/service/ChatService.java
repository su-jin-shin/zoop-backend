package com.example.demo.chat.service;

import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.chat.constants.Constants;
import com.example.demo.chat.constants.ErrorMessages;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.*;
import com.example.demo.chat.exception.ChatRoomNotFoundException;
import com.example.demo.chat.exception.ChatServiceException;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.chat.type.SenderType;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ChatUpdateService chatUpdateService;

    // 채팅방 생성
    @Transactional
    public ChatRoomResponseDto createChatRoom(Long userId) {
        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = new ChatRoom(userInfo);
        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(saved.getChatRoomId(), saved.getCreatedAt());
    }



    // 메시지 테이블에 추천 리스트 update
    @Transactional
    public void updateMessage(Long messageId, List<PropertyExcelDto> properties) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메시지를 찾을 수 없습니다. ID: " + messageId));
        message.updateProperties(properties);
    }

    // 메시지 저장Add commentMore actions
    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto) {
        try {
            ChatRoom chatRoom = findByChatRoomId(messageRequestDto.getChatRoomId(), ErrorMessages.CHAT_SAVE_MESSAGE_FAILED); // 채팅방의 존재 여부를 확인하여, 없으면 예외 발생 (EntityNotFoundException)
            Message message = new Message(chatRoom, messageRequestDto.getSenderType(), messageRequestDto.getContent());
            Message saved =  messageRepository.save(message);
            chatRoom.updateLastMessageAt(saved.getCreatedAt()); // 채팅방의 마지막 메시지 발송 시각을 갱신
            return new MessageResponseDto(messageRequestDto.getChatRoomId(), saved.getMessageId(), saved.getCreatedAt());
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_SAVE_MESSAGE_FAILED, messageRequestDto.getChatRoomId(), e);
        }
    }

    // 메시지 저장
    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto, List<PropertyExcelDto> properties) {
        try {
            ChatRoom chatRoom = findByChatRoomId(messageRequestDto.getChatRoomId(), ErrorMessages.CHAT_SAVE_MESSAGE_FAILED); // 채팅방의 존재 여부를 확인하여, 없으면 예외 발생 (EntityNotFoundException)
            Message message = new Message(chatRoom, messageRequestDto.getSenderType(), messageRequestDto.getContent(), properties);
            Message saved =  messageRepository.save(message);
            chatRoom.updateLastMessageAt(saved.getCreatedAt()); // 채팅방의 마지막 메시지 발송 시각을 갱신
            return new MessageResponseDto(messageRequestDto.getChatRoomId(), saved.getMessageId(), saved.getCreatedAt());
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_SAVE_MESSAGE_FAILED, messageRequestDto.getChatRoomId(), e);
        }
    }

    private ChatRoom findByChatRoomId(Long chatRoomId, String context) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId, context));
    }

    // 필터 조건으로 채팅방 제목 변경
    @Transactional
    public ChatRoomResponseDto updateChatRoomTitle(ChatRoomRequestDto chatRoomRequestDto) {
        try {
            ChatRoom chatRoom = findByChatRoomId(chatRoomRequestDto.getChatRoomId(), ErrorMessages.CHAT_UPDATE_TITLE_FAILED);
            chatRoom.updateTitle(chatRoomRequestDto.getTitle());

            // 응답 DTO로 가공
            return new ChatRoomResponseDto(
                    chatRoom.getChatRoomId(),
                    chatRoom.getTitleUpdatedAt(),
                    chatRoom.getTitle()
            );
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_UPDATE_TITLE_FAILED, chatRoomRequestDto.getChatRoomId(), e);
        }
    }

    // 사용자가 원하는 채팅방 제목 변경
    @Transactional
    public void updateChatRoomTitle(Long chatRoomId, String title) {
        try {
            ChatRoom chatRoom = findByChatRoomId(chatRoomId, ErrorMessages.CHAT_UPDATE_TITLE_FAILED);
            chatRoom.updateTitle(title);
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_UPDATE_TITLE_FAILED, chatRoomId, e);
        }
    }

    // 처음 필터 생성시 채팅방 제목 저장
    @Transactional
    public ChatRoomResponseDto updateTitle(Long chatRoomId, FilterRequestDto filterRequestDto) {


        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException());

        // 여기서 필터 제목으로 직접 갱신
        chatRoom.updateTitle(filterRequestDto.buildFilterTitle());

        ChatRoom saved = chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.fromEntity(saved);
    }


    // 채팅방 삭제 (소프트 삭제)
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        try {
            findByChatRoomId(chatRoomId, ErrorMessages.CHAT_DELETE_FAILED);
            chatRoomRepository.softDeleteChatRoom(chatRoomId, LocalDateTime.now());
            messageRepository.softDeleteMessages(chatRoomId, LocalDateTime.now()); // 메시지도 소프트 삭제
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_DELETE_FAILED, chatRoomId, e);
        }
    }

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

    // 로그인한 사용자가 특정 채팅방 선택시 채팅내용 조회
    @Transactional(readOnly = true)
    public ChatRoomDetailResponseDto getUserChatRoomMessages(Long userId, Long chatRoomId) {

        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        // 사용자의 채팅방인지 확인 후 조회
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndUserInfo(chatRoomId, userInfo);
        if (chatRoom == null) {
            throw new NotFoundException();
        }

        // 로그인한 유저가 특정 채팅방을 조회할 때 삭제되지 않은 메세지만 조회
        List<Message> messages = messageRepository.findByChatRoom_ChatRoomIdAndDeletedAtIsNullOrderByMessageId(chatRoomId);

        // 메시지 DTO로 변환
        List<ChatRoomDetailResponseDto.MessageDto> messageDtos = messages.stream()
                .map(ChatRoomDetailResponseDto.MessageDto::from)
                .toList();

        // 응답 DTO 생성
        return ChatRoomDetailResponseDto.from(chatRoom, messageDtos);
    }

    // 채팅방 검색(제목 및 채팅내용으로 검색)
    @Transactional(readOnly = true)
    public List<ChatRoomRequestDto> searchChatRooms(Long userId, String searchText) {

        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        // 대소문자 구분없이 조회
        String keyword = searchText == null ? "" : searchText.trim().toLowerCase();

        // 삭제되지 않은 채팅방 전체 조회 (생성일 내림차순 정렬)
        return chatRoomRepository.findByUserInfoAndIsDeletedFalseOrderByCreatedAtDesc(userInfo).stream()
                .map(chatRoom -> {
                    String title = chatRoom.getTitle();
                    boolean matchesTitle = title != null && title.toLowerCase().contains(keyword);

                    // 사용자가 검색한 검색어와 매칭되는 채팅방 내용중에서 가장 최신의 데이터 담기
                    Optional<Message> lastMatchingMessage = messageRepository
                            .findTopByChatRoomAndContentContainingIgnoreCaseOrderByMessageIdDesc(chatRoom, keyword);

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

    @Async
    public void generateAndSaveAiResponse(Long userId, MessageRequestDto request,
                                          Constants.MessageResultType messageResultType,
                                          List<PropertyExcelDto> recommendedProperties) {

        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        String nickname = userInfo.getNickname();

        List<MessageReplyDto> aiReplies = new ArrayList<>();
        if (messageResultType == Constants.MessageResultType.FAILURE) {
            aiReplies.add(new MessageReplyDto().generateAiResponse(Constants.NO_MATCHING_PROPERTY_MESSAGE, null, false));
        } else {
            aiReplies.add(new MessageReplyDto().generateAiResponse(
                    String.format(Constants.MATCHING_PROPERTY_MESSAGE, nickname, recommendedProperties.size()), null, false));

            aiReplies.add(new MessageReplyDto().generateAiResponse(null, recommendedProperties, true));
            aiReplies.add(new MessageReplyDto().generateAiResponse(Constants.ADDITIONAL_FILTER_PROMPT, null, false));
        }


        for (MessageReplyDto aiReply : aiReplies) {
            MessageRequestDto aiRequest = new MessageRequestDto(
                    request.getChatRoomId(),
                    aiReply.getContent(),
                    SenderType.CHATBOT
            );

            MessageResponseDto aiMessage = saveMessage(aiRequest, aiReply.getProperties());

            chatUpdateService.notifyNewMessage(MessageDto.builder()
                    .chatRoomId(aiRequest.getChatRoomId())
                    .messageId(aiMessage.getMessageId())
                    .senderType(SenderType.CHATBOT)
                    .content(aiReply.getContent())
                    .properties(aiReply.getProperties())
                    .createdAt(aiMessage.getCreatedAt())
                    .build());
        }
    }

}