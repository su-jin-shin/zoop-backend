package com.example.demo.mypage.repository;

import com.example.demo.mypage.dto.MyCommentQueryDto;
import com.example.demo.review.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query("""
    SELECT 
        rc.id AS commentId,
        rc.content AS content,
        cast(rc.createdAt as date) AS createdAt,
        r.id AS reviewId,
        r.content AS reviewContent,
        c.id AS complexId,
        r.propertyId AS propertyId,
        COALESCE(c.complexName, 
            (SELECT p.articleName FROM Property p WHERE p.id = r.propertyId)
        ) AS articleName,
        (SELECT COUNT(l) FROM ReviewCommentLike l 
         WHERE l.reviewComment.id = rc.id AND l.isLiked = true) AS likeCount
    FROM ReviewComment rc
    JOIN rc.review r
    LEFT JOIN r.complex c
    WHERE rc.user.userId = :userId AND rc.deletedAt IS NULL
    ORDER BY rc.createdAt DESC
""")
    List<MyCommentQueryDto> findMyComments(@Param("userId") Long userId);
}