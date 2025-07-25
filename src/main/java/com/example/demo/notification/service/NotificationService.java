package com.example.demo.notification.service;

import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    // 알림 목록 조회
    List<NotificationResponseDto> findByUserIdAndIsUsedTrue(Long userId);

    // 특정 알림을 '읽음' 처리
    void read(Long userId, Long notificationId);

}
