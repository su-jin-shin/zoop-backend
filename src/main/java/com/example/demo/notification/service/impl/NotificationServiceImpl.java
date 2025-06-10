package com.example.demo.notification.service.impl;

import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 목록 조회
    @Override
    public List<NotificationResponseDto> findByUserIdAndIsUsedTrue(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsUsedTrue(userId);
        return notifications.stream()
                .map(NotificationResponseDto::createNotificationResponseDto)
                .toList();
    }
}
