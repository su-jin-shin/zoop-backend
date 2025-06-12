package com.example.demo.review.mapper;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentMapper {

    private final ReviewCommentLikeRepository reviewCommentLikeRepo;

    /**
     * Entity → DTO (단일 댓글)
     */
    public ReviewCommentResponse toDto(ReviewComment comment, UserInfo currentUser) {
        long likeCount = reviewCommentLikeRepo.countByReviewCommentIdAndIsLikedTrue(comment.getId());

        boolean isLikedByMe = reviewCommentLikeRepo
                .findByReviewCommentIdAndUser(comment.getId(), currentUser)
                .map(ReviewCommentLike::isLiked)
                .orElse(false);

        boolean isMine = comment.isMine(currentUser);

        return ReviewCommentResponse.builder()
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
     * CreateRequest → Entity
     */
    public ReviewComment toEntity(ReviewCommentCreateRequest request, UserInfo user, Review review) {
        return ReviewComment.builder()
                .user(user)
                .review(review)
                .content(request.getContent())
                .build();
    }

    /**
     * UpdateRequest → Entity 변경
     */
    public void updateEntity(ReviewComment comment, ReviewCommentUpdateRequest request) {
        comment.updateContent(request.getContent());
    }

    /**
     * 댓글 좋아요 생성용 → Entity
     */
    public ReviewCommentLike toEntity(ReviewComment comment, UserInfo user, boolean isLiked) {
        return ReviewCommentLike.builder()
                .reviewComment(comment)
                .user(user)
                .isLiked(isLiked)
                .build();
    }

    /**
     * ❌ (보류) Page → ListResponse는 지금 사용하지 않으므로 제거하거나 남겨두기
     */
    // public ReviewCommentListResponse toListResponse(...) { ... }


    // 2. Entity 리스트 → ListResponse DTO
    public ReviewCommentListResponse toListResponse(Page<ReviewComment> commentPage, Long reviewId, UserInfo currentUser) {
        List<ReviewCommentResponse> commentResponses = commentPage.getContent().stream()
                .map(comment -> toDto(comment, currentUser))
                .toList();

        return ReviewCommentListResponse.builder()
                .reviewId(reviewId)
                .commentCount(commentPage.getTotalElements())
                .comments(commentResponses)
                .page(commentPage.getNumber())
                .size(commentPage.getSize())
                .totalCount(commentPage.getTotalElements())
                .build();
    }
}