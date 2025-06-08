package com.example.demo.notification.repository;

import com.example.demo.Filter.domain.QKeywordFilterHistory;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.domain.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 알림 목록 조회
    @Override
    public List<Notification> findByUserIdAndIsUsedTrue(Long userId) {
        QNotification n = QNotification.notification;
        QKeywordFilterHistory k = QKeywordFilterHistory.keywordFilterHistory;

        return queryFactory
                .selectFrom(n)
                .join(n.keywordFilterHistory, k)
                .where(
                        k.userInfo.userId.eq(userId),
                        k.isUsed.isTrue()
                )
                .fetch();
    }
}
