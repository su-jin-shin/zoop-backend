package com.example.demo.review.repository;
/*
목적 : 리뷰에 대한 좋아요 상태 관리
메서드 : 좋아요 여부/개수 조회, 토글 등록/수정, 개수 조회 등
 */
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.review.domain.ReviewLike;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.util.*;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndUser(Long reviewId, UserInfo user);

    long countByReviewIdAndIsLikedTrue(Long reviewId);

    //  1. 리뷰 ID 목록에 대해 좋아요 수 조회
    @Query("SELECT rl.review.id, COUNT(rl) FROM ReviewLike rl " +
            "WHERE rl.isLiked = true AND rl.review.id IN :reviewIds " +
            "GROUP BY rl.review.id")
    List<Object[]> countLikesByReviewIds(List<Long> reviewIds);

    //  2. 특정 유저가 누른 좋아요 여부 map
    @Query("SELECT rl.review.id, rl.isLiked FROM ReviewLike rl " +
            "WHERE rl.user = :user AND rl.review.id IN :reviewIds")
    List<Object[]> findIsLikedInfo(List<Long> reviewIds, UserInfo user);

    //  3. default 메서드로 Map 반환
    default Map<Long, Long> countLikesMap(List<Long> reviewIds) {
        List<Object[]> rows = countLikesByReviewIds(reviewIds);
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Long) row[1]);
        }
        return map;
    }

    default Map<Long, Boolean> getIsLikedMapByReviewIds(List<Long> reviewIds, UserInfo user) {
        List<Object[]> rows = findIsLikedInfo(reviewIds, user);
        Map<Long, Boolean> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Boolean) row[1]);
        }
        return map;
    }
}


