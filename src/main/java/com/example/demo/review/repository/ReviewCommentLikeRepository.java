package com.example.demo.review.repository;

/*
목적 : 댓글에 대한 좋아요 처리
메서드 : 좋아요 여부/개수 조회, 토글 등록/수정, 개수 조회 등
 */

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.review.domain.ReviewCommentLike;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ReviewCommentLikeRepository extends JpaRepository<ReviewCommentLike, Long> {

    // 좋아요 여부
    Optional<ReviewCommentLike> findByReviewCommentIdAndUser(Long commentId, UserInfo user);

    //Optional<ReviewCommentLike> findByReviewCommentAndUser(ReviewComment reviewComment, UserInfo user);

    // 좋아요 개수
    long countByReviewCommentIdAndIsLikedTrue(Long commentId);


}
