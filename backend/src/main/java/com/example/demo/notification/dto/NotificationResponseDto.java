package com.example.demo.notification.dto;


import com.example.demo.notification.domain.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;
    private String content;        // 알림 메세지
    private String filterTitle;    // 아림 메세지 제목
    private Boolean isRead;        // 알림 읽음 여부
    private LocalDateTime usedAt;  // 사용한 시간


    public static NotificationResponseDto createNotificationResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .content(notification.getContent())
                .filterTitle(notification.getKeywordFilterHistory().getFilter().getFilterTitle())
                .isRead(notification.isRead())
                .usedAt(notification.getKeywordFilterHistory().getUsedAt())
                .build();

    }
}
