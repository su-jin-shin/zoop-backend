package com.example.demo.notification.service.impl;

import com.example.demo.notification.repository.SseEmitterRepository;
import com.example.demo.notification.service.SseEmitterService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class SseEmitterServiceImpl implements SseEmitterService {

    private final SseEmitterRepository sseEmitterRepository;

    private Long timeout = 60L * 1000L * 60L;  // 1시간

    private static final String CONNECT_MESSAGE = "connected!";

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        // 기존 연결을 모두 삭제하여 중복 연결 방지
        sseEmitterRepository.deleteAllEmitterStartWithId(String.valueOf(userId));

        String emitterId = createEmitterId(userId);
        // SseEmitter 객체 생성. timeout 값 설정
        SseEmitter emitter = new SseEmitter(timeout);
        // 생성된 emitter를 레포지토리에 저장
        sseEmitterRepository.save(emitterId, emitter);
        // 초기 연결 메시지 전송 (503 에러 방지 및 연결 확인용)
        sendToClient(emitter, emitterId, CONNECT_MESSAGE);

        // Emitter가 완료되었을 때 (클라이언트 연결 종료 시)
        emitter.onCompletion(() -> {
            log.info("Emitter 완료됨: {}", emitterId);
            sseEmitterRepository.deleteById(emitterId); // 레포지토리에서 해당 emitter 삭제
        });

        // Emitter 타임아웃 발생 시
        emitter.onTimeout(() -> {
            log.warn("Emitter 타임아웃 발생: {}", emitterId);
            sseEmitterRepository.deleteById(emitterId); // 레포지토리에서 해당 emitter 삭제
            emitter.complete(); // emitter 완료 처리
        });

        // Emitter 에러 발생 시
        emitter.onError(throwable -> {
            log.error("Emitter 에러 발생: {} - {}", emitterId, throwable.getMessage(), throwable);
            sseEmitterRepository.deleteById(emitterId); // 레포지토리에서 해당 emitter 삭제
            emitter.complete(); // emitter 완료 처리
        });

        // 클라이언트가 미수신한 이벤트가 있을 경우 재전송
        sendAfterLastEventId(userId, lastEventId, emitter);

        return emitter;
    }

    // 이벤트 캐시를 저장
    @Override
    public void saveEventCache(String emitterId, Object event) {
        sseEmitterRepository.saveEventCache(emitterId, event);
    }

    // 클라이언트로 실제 알림을 전송하는 private 헬퍼 메서드
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
            saveEventCache(emitterId, data);
        } catch (IOException | IllegalStateException ex) {
            log.warn("클라이언트 전송 실패 또는 연결 끊김: {} - {}", emitterId, ex.getMessage(), ex);
            // 알림 전송 중 에러 발생 시 해당 emitter 삭제
            sseEmitterRepository.deleteById(emitterId);
        } catch(Exception ex){
            log.error("알 수 없는 오류 발생 ({}): {}", emitterId, ex.getMessage(), ex);
            sseEmitterRepository.deleteById(emitterId);
        }
    }

    // Emitter 및 이벤트 ID를 생성하는 헬퍼 메서드
    private String createEmitterId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    // 마지막으로 수신한 이벤트 ID 이후의 유실된 이벤트를 재전송하는 헬퍼 메서드
    private void sendAfterLastEventId(Long userId, String lastEventId, SseEmitter emitter) {
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = sseEmitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
    }

}
