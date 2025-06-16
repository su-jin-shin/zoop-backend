package com.example.demo.review.mapper;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentMapper {

    private final ReviewCommentLikeRepository likeRepository;

    public ReviewCommentResponse toDto(ReviewComment comment, LoginUser loginUser) {
        boolean isLiked = loginUser != null && likeRepository
                .findByReviewCommentIdAndUser(comment.getId(), loginUser.getUserInfo())
                .map(ReviewCommentLike::isLiked)
                .orElse(false);

        boolean isMine = loginUser != null &&
                comment.getUser().getUserId().equals(loginUser.getUserId());

        return ReviewCommentResponse.builder()
                .commentId(comment.getId())
                .reviewId(comment.getReview().getId())
                .userId(comment.getUser().getUserId())
                .nickname(comment.getUser().getNickname())
                .profileImage(comment.getUser().getProfileImage())
                .content(comment.getContent())
                .likeCount(likeRepository.countByReviewCommentIdAndIsLikedTrue(comment.getId()))
                .isLikedByMe(isLiked)
                .isMine(isMine)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public ReviewComment toEntity(ReviewCommentCreateRequest request, LoginUser loginUser, Review review) {
        return ReviewComment.builder()
                .review(review)
                .user(loginUser.getUserInfo())
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
}





