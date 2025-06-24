package com.example.demo.review.mapper;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentMapper {

    /**
     * @ 댓글 엔티티를 응답 DTO로 변환
     * @ 좋아요 수, 상태, 작성자 여부 포함
     */
    public ReviewCommentCreateResponse toDto(ReviewComment comment,
                                             long likeCount,
                                             boolean isLikedByMe,
                                             boolean isMine) {
        if (comment.getReview() == null) {
            throw new IllegalStateException("댓글에 연결된 리뷰가 null입니다. commentId=" + comment.getId());
        }

        return ReviewCommentCreateResponse.builder()
                .commentId(comment.getId())
                .reviewId(comment.getReview().getId())
                .userId(comment.getUser().getUserId())
                .nickname(comment.getUser().getNickname())
                .profileImage(comment.getUser().getProfileImage())
                .content(comment.getContent())
                .likeCount(likeCount)
                .isLikedByMe(isLikedByMe)
                .isMine(isMine)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }


    /**
     * @ 댓글 생성 요청 DTO를 엔티티로 변환
     * @ 작성자, 리뷰 연결 포함
     */
    public ReviewComment toEntity(ReviewCommentCreateRequest request, UserInfo loginUser, Review review) {
        return ReviewComment.builder()
                .review(review)
                .user(loginUser)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}


