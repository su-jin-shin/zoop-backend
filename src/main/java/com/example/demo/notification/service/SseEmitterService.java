package com.example.demo.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

    // 클라이언트가 SSE 구독을 요청할 때 호출된다.
    SseEmitter subscribe(Long userId, String lastEventId);

    void saveEventCache(String emitterId, Object event);

}
