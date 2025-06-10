package com.example.demo.notification.repository.impl;

import com.example.demo.notification.repository.SseEmitterRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor
@Repository
public class SseEmitterRepositoryImpl implements SseEmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // 새로운 SseEmitter 객체를 저장
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId,sseEmitter);
        return sseEmitter;
    }

    // 특정 SseEmitter와 관련된 이벤트를 캐시에 저장
    @Override
    public void saveEventCache(String emitterId, Object event) {
        eventCache.put(emitterId,event);
    }


    // 특정 사용자 ID로 시작하는 모든 이벤트 캐시 데이터를 찾아 반환
    @Override
    public Map<String, Object> findAllEventCacheStartWithByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 특정 emitterId에 해당하는 SseEmitter 객체를 삭제
    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    // 특정 사용자 ID로 시작하는 모든 SseEmitter 객체들을 삭제
    @Override
    public void deleteAllEmitterStartWithId(String userId) {
        emitters.forEach(
                (key,emitter) -> {
                    if(key.startsWith(userId)){
                        emitters.remove(key);
                    }
                }
        );
    }
}
