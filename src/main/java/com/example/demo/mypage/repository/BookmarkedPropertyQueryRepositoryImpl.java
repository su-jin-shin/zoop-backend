package com.example.demo.mypage.repository;

import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.mypage.domain.QBookmarkedProperty;
import com.example.demo.property.domain.QProperty;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookmarkedPropertyQueryRepositoryImpl implements BookmarkedPropertyQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookmarkedProperty> findAllWithPropertyByUserId(Long userId) {
        log.info("🚨 QueryDSL 호출됨: userId = {}", userId);
        QBookmarkedProperty bp = QBookmarkedProperty.bookmarkedProperty;
        QProperty p = QProperty.property;

        return queryFactory
                .selectFrom(bp)
                .distinct()
                .join(bp.property, p).fetchJoin()
                .join(bp.user).fetchJoin()
                .where(
                        bp.user.userId.eq(userId),
                        bp.isBookmarked.isTrue()
                )
                .fetch();
    }
}
