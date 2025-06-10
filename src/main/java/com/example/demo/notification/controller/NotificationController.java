package com.example.demo.notification.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.notification.service.SseEmitterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterService sseEmitterService;

    private static final String X_ACCEL_BUFFERING = "X-Accel-Buffering";
    private static final String LAST_EVENT_ID = "Last-Event-ID";
    private static final String X_ACCEL_BUFFERING_VALUE = "no";

    @GetMapping()
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications(@AuthenticationPrincipal LoginUser loginUser) {

        // 로그인한 유저의 userId 추출
        Long userId = Long.valueOf(loginUser.getUsername());

        // 서비스에서 알림 목록 조회
        List<NotificationResponseDto> notifications = notificationService.findByUserIdAndIsUsedTrue(userId);

        return ResponseEntity.ok(notifications);

    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal LoginUser loginUser,
    HttpServletResponse response,
    @RequestHeader(value = LAST_EVENT_ID, required = false, defaultValue = "") String lastEventId) {
        response.setHeader(X_ACCEL_BUFFERING, X_ACCEL_BUFFERING_VALUE);
        Long userId = Long.valueOf(loginUser.getUsername());

        return ResponseEntity.ok(sseEmitterService.subscribe(userId, lastEventId));
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<?> read(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long notificationId) {
        Long userId = Long.valueOf(loginUser.getUsername());
        notificationService.read(userId, notificationId);

        return ResponseEntity.ok().build();
    }
}
