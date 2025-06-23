package com.example.demo.chat.service;

import com.example.demo.chat.dto.MessageDto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatUpdateService {
    private final Map<Long, List<DeferredResult<List<MessageDto>>>> chatListeners = new ConcurrentHashMap<>();
    private final Map<Long, List<MessageDto>> pendingMessages = new ConcurrentHashMap<>();

    public void register(Long chatRoomId, DeferredResult<List<MessageDto>> result) {
        chatListeners.computeIfAbsent(chatRoomId, k -> new ArrayList<>()).add(result);

        // 기존에 대기 중이던 메시지가 있으면 즉시 응답
        List<MessageDto> queued = pendingMessages.getOrDefault(chatRoomId, new ArrayList<>());
        if (!queued.isEmpty()) {
            result.setResult(new ArrayList<>(queued));
            pendingMessages.put(chatRoomId, new ArrayList<>()); // 초기화
            chatListeners.get(chatRoomId).remove(result); // 제거
            return;
        }

        result.onCompletion(() -> {
            chatListeners.get(chatRoomId).remove(result);
        });
    }

    public void notifyNewMessage(MessageDto message) {
        Long chatRoomId = message.getChatRoomId();
        List<DeferredResult<List<MessageDto>>> listeners = chatListeners.get(chatRoomId);

        if (listeners != null && !listeners.isEmpty()) {
            for (DeferredResult<List<MessageDto>> listener : listeners) {
                listener.setResult(List.of(message)); // 하나라도 있으면 즉시 응답
            }
            listeners.clear();
        } else {
            // 리스너 없으면 메시지 큐에 쌓아두기
            pendingMessages.computeIfAbsent(chatRoomId, k -> new ArrayList<>()).add(message);
        }
    }
}
