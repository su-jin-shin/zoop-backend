package com.example.demo.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseEmitterRepository {

    // 새로운 SseEmitter 객체를 저장
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    // 특정 SseEmitter와 관련된 이벤트를 캐시에 저장
    void saveEventCache(String emitterId, Object event);

    // 특정 사용자 ID로 시작하는 모든 이벤트 캐시 데이터를 찾아 반환
    Map<String,Object> findAllEventCacheStartWithByUserId(String userId);

    // 특정 emitterId에 해당하는 SseEmitter 객체를 삭제
    void deleteById(String emitterId);

    // 특정 사용자 ID로 시작하는 모든 SseEmitter 객체들을 삭제
    void deleteAllEmitterStartWithId(String userId);

}
