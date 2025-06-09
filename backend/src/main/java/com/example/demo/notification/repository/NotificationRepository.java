package com.example.demo.notification.repository;

import com.example.demo.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository  extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{

    // 알림 목록 조회
    List<Notification> findByUserIdAndIsUsedTrue(Long userId);
}
