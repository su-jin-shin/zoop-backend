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

    public ReviewCommentCreateResponse toDto(ReviewComment comment,
                                             long likeCount,
                                             boolean isLikedByMe,
                                             boolean isMine) {
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

    public ReviewComment toEntity(ReviewCommentCreateRequest request, UserInfo loginUser, Review review) {
        return ReviewComment.builder()
                .review(review)
                .user(loginUser)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ReviewComment updateEntity(ReviewComment comment, ReviewCommentUpdateRequest request) {
        comment.updateContent(request.getContent());
        return comment;
    }

    public ReviewCommentLike toEntity(ReviewComment comment, UserInfo user, boolean isLiked) {
        return ReviewCommentLike.builder()
                .reviewComment(comment)
                .user(user)
                .isLiked(isLiked)
                .build();
    }

    public ReviewCommentLikeResponse toDto(ReviewComment comment, UserInfo user, boolean isLiked, long likeCount) {
        return ReviewCommentLikeResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(comment.getId())
                .userId(user.getUserId())
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }

}


