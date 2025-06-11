package com.example.demo.notification.service.impl;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserInfoRepository userInfoRepository;

    // 알림 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> findByUserIdAndIsUsedTrue(Long userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        List<Notification> notifications = notificationRepository.findByUserIdAndIsUsedTrue(userInfo.getUserId());
        return notifications.stream()
                .map(NotificationResponseDto::createNotificationResponseDto)
                .toList();
    }

    // 특정 알림 '읽음' 처리
    @Override
    public void read(Long userId, Long notificationId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(InvalidRequestException::new);
        isNotficationAlreadyRead(notification);
        isUserOfNotification(notification, userInfo);
        notification.readNotification();
    }

    private void isUserOfNotification(Notification notification, UserInfo userInfo) {
        Long userId = notification.getKeywordFilterHistory().getUserInfo().getUserId();
        if(!userId.equals(userInfo.getUserId())) {
            throw new InvalidRequestException();
        }
    }

    private void isNotficationAlreadyRead(Notification notification) {
        if(notification.isRead()) {
            throw new UnauthorizedAccessException();
        }
    }
}
