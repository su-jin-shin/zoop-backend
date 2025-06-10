package com.example.demo.notification.service.impl;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
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
    private final UserInfoRepository userInfoRepository;

    // 알림 목록 조회
    @Override
    public List<NotificationResponseDto> findByUserIdAndIsUsedTrue(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsUsedTrue(userId);
        return notifications.stream()
                .map(NotificationResponseDto::createNotificationResponseDto)
                .toList();
    }

    // 특정 알림 '읽음' 처리
    @Override
    public void read(Long userId, Long notificationId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow();
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        isNotficationAlreadyRead(notification);
        isUserOfNotification(notification, userInfo);
        notification.readNotification();
    }

    private void isUserOfNotification(Notification notification, UserInfo userInfo) {
        Long userId = notification.getKeywordFilterHistory().getUserInfo().getUserId();
        if(!userId.equals(userInfo.getUserId())) {
            throw new RuntimeException();
        }
    }

    private void isNotficationAlreadyRead(Notification notification) {
        if(notification.isRead()) {
            throw new RuntimeException();
        }
    }
}
