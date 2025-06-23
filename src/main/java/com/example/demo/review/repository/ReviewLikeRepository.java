package com.example.demo.review.repository;
/*
 * ReviewLikeRepository
 *
 * 목적: 리뷰에 대한 좋아요 상태 관리
 *
 * 주요 기능:
 * - 특정 리뷰에 대한 좋아요 여부 및 개수 조회
 * - 다수 리뷰의 좋아요 수/상태를 Map 형태로 일괄 조회
 * - 좋아요 상태 등록/수정은 서비스 단에서 수행
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

    /**
     * 특정 리뷰에 대해 사용자가 누른 좋아요 정보 조회
     * - 중복 저장 방지를 위해 (리뷰ID + 사용자) 조합으로 유일함
     */
    Optional<ReviewLike> findByReviewIdAndUser(Long reviewId, UserInfo user);


    /**
     * 특정 리뷰에 대해 좋아요 수 조회
     * - isLiked = true 조건
     */
    long countByReviewIdAndIsLikedTrue(Long reviewId);

    /**
     * 여러 리뷰 ID에 대해 좋아요 수를 집계
     * - 삭제 여부와 관계없이 isLiked = true 기준
     */
    @Query("SELECT rl.review.id, COUNT(rl) FROM ReviewLike rl " +
            "WHERE rl.isLiked = true AND rl.review.id IN :reviewIds " +
            "GROUP BY rl.review.id")
    List<Object[]> countLikesByReviewIds(List<Long> reviewIds);

    /**
     * 사용자가 누른 여러 리뷰의 좋아요 여부 조회
     * - 리뷰 ID 기준으로 사용자별 상태 확인
     */
    @Query("SELECT rl.review.id, rl.isLiked FROM ReviewLike rl " +
            "WHERE rl.user = :user AND rl.review.id IN :reviewIds")
    List<Object[]> findIsLikedInfo(List<Long> reviewIds, UserInfo user);

    /**
     *  [기본 메서드 1]
     * countLikesByReviewIds 결과를 Map 형태로 변환
     */
    default Map<Long, Long> countLikesMap(List<Long> reviewIds) {
        List<Object[]> rows = countLikesByReviewIds(reviewIds);
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Long) row[1]);
        }
        return map;
    }

    /**
     *  [기본 메서드 2]
     * findIsLikedInfo 결과를 Map 형태로 변환
     */
    default Map<Long, Boolean> getIsLikedMapByReviewIds(List<Long> reviewIds, UserInfo user) {
        List<Object[]> rows = findIsLikedInfo(reviewIds, user);
        Map<Long, Boolean> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Boolean) row[1]);
        }
        return map;
    }
}


