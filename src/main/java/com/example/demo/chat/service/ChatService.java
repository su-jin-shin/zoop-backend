package com.example.demo.chat.service;

import com.example.demo.chat.constants.ErrorMessages;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.dto.MessageDto;
import com.example.demo.chat.exception.ChatRoomNotFoundException;
import com.example.demo.chat.exception.ChatServiceException;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.chat.type.SenderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 채팅방 생성
    @Transactional
    public Long createChatRoom(Long userId) {
        try {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setUserId(userId);
            ChatRoom saved =  chatRoomRepository.save(chatRoom);
            return saved.getChatRoomId();
        } catch (Exception e) {
            throw new ChatServiceException(String.format("%s. userId=%d", ErrorMessages.CHAT_CREATE_FAILED, userId), e);
        }
    }

    private ChatRoom findByChatRoomId(Long chatRoomId, String context) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId, context));
    }

    // 메시지 저장
    @Transactional
    public MessageDto saveMessage(MessageDto messageDto) {
        try {
            ChatRoom chatRoom = findByChatRoomId(messageDto.getChatRoomId(), ErrorMessages.CHAT_SAVE_MESSAGE_FAILED); // 채팅방의 존재 여부를 확인하여, 없으면 예외 발생 (EntityNotFoundException)
            Message message = new Message(chatRoom, messageDto.getSenderType(), messageDto.getContent());
            Message saved =  messageRepository.save(message);
            chatRoom.updateLastMessageAt(saved.getCreatedAt()); // 채팅방의 마지막 메시지 발송 시각을 갱신
            return new MessageDto(saved.getMessageId(), saved.getContent(), saved.getCreatedAt());
        } catch (Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_SAVE_MESSAGE_FAILED, messageDto.getChatRoomId(), e);
        }
    }

    // 채팅방 제목 변경
    @Transactional
    public void updateChatRoomTitle(Long chatRoomId, String title) {
        try {
            ChatRoom chatRoom = findByChatRoomId(chatRoomId, ErrorMessages.CHAT_UPDATE_TITLE_FAILED);
            chatRoom.updateTitle(title);
        } catch(Exception e) {
            throw new ChatServiceException(ErrorMessages.CHAT_UPDATE_TITLE_FAILED, chatRoomId, e);
        }
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
    public List<ChatRoomDto> getUserChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(userId);

        List<ChatRoomDto> result = new ArrayList<>();
        int order = 0;

        for (ChatRoom c : chatRooms) {
            result.add(
                ChatRoomDto.builder()
                        .order(++order)
                        .chatRoomId(c.getChatRoomId())
                        .title(c.getTitle())
                        .lastMessageAt(c.getLastMessageAt())
                        .build()
            );
        }
        return result;
    }

}
