package com.example.demo.review.repository;
/*
목적 : 리뷰에 대한 좋아요 상태 관리
메서드 : 좋아요 여부/개수 조회, 토글 등록/수정, 개수 조회 등
 */
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    // 특정 유저가 특정 리뷰에 좋아요 눌렀는지
    Optional<ReviewLike> findByReviewIdAndUser(Long reviewId,UserInfo user);

    // 특정 리뷰 좋아요 개수
    long countByReviewIdAndIsLikedTrue(Long reviewId);

}
