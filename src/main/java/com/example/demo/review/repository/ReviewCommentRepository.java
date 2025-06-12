package com.example.demo.review.repository;

/*
목적 : 리뷰에 달린 댓글 처리
메서드 : 댓글 목록/개수 조회, 삭제, 수정 등
 */

import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 특정 리뷰에 달린 댓글 목록
    @Query("SELECT rc FROM ReviewComment rc WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL")
    List<ReviewComment> findByReviewId(@Param("reviewId") Long reviewId);


    // 본인이 작성한 댓글 조회
    @Query("SELECT rc FROM ReviewComment rc WHERE rc.user.userId = :userId")
    List<ReviewComment> findByUserId(@Param("userId") Long userId);

    // 댓글 개수 조회 (삭제되지 않은 것만)
    @Query("SELECT COUNT(rc) FROM ReviewComment rc WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL")
    long commentCount(@Param("reviewId") Long reviewId);

    // 단일 댓글 조회
    Optional<ReviewComment> findById(Long commentId);




}
