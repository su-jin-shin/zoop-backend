package com.example.demo.notification.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;
import com.example.demo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications(@AuthenticationPrincipal LoginUser loginUser) {

        // 로그인한 유저의 userId 추출
        Long userId = Long.valueOf(loginUser.getUsername());

        // 서비스에서 알림 목록 조회
        List<NotificationResponseDto> notifications = notificationService.findByUserIdAndIsUsedTrue(userId);

        return ResponseEntity.ok(notifications);

    }
}
