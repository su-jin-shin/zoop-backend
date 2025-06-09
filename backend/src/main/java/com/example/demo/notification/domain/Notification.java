package com.example.demo.notification.domain;

import com.example.demo.Filter.domain.KeywordFilterHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    private KeywordFilterHistory keywordFilterHistory;    // 키워드 필터 히스토리 테이블과 관계 맺기

    private String content;           // 알림 내용

    private boolean isRead;           // 읽음 여부

    public void readNotification() {
        this.isRead = true;
    }
}
