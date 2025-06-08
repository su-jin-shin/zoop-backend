package com.example.demo.notification.repository;

import com.example.demo.notification.domain.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    // 알림 목록 조회
    List<Notification> findByUserIdAndIsUsedTrue(Long userId);
}
