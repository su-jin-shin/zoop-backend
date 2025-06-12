package com.example.demo.notification.repository.impl;

import com.example.demo.Filter.domain.QKeywordFilterHistory;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.domain.QNotification;
import com.example.demo.notification.repository.NotificationRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "queryFactory는 DI로 주입된 불변 객체이며 외부 노출 위험이 없음"
)
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
