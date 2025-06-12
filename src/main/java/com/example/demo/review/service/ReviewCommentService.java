package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.mapper.ReviewCommentMapper;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import com.example.demo.review.repository.ReviewCommentRepository;
import com.example.demo.review.repository.ReviewRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;
    private final ReviewCommentLikeRepository likeRepository;
    private final ReviewCommentMapper commentMapper;

    /**
     * 댓글 단건 조회
     */
    public ReviewCommentResponse getCommentById(Long commentId, UserInfo currentUser) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        return commentMapper.toDto(comment, currentUser);
    }

    /**
     * 댓글 목록 조회 (페이징 없이 전체 반환)
     */
    public List<ReviewCommentResponse> getComments(Long reviewId, UserInfo currentUser) {
        List<ReviewComment> comments = commentRepository.findByReviewId(reviewId);
        return comments.stream()
                .map(comment -> commentMapper.toDto(comment, currentUser))
                .toList();
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public ReviewCommentResponse createComment(Long reviewId, UserInfo currentUser, ReviewCommentCreateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        ReviewComment comment = commentMapper.toEntity(request, currentUser, review);
        commentRepository.save(comment);

        return commentMapper.toDto(comment, currentUser);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public ReviewCommentResponse updateComment(Long commentId, UserInfo currentUser, ReviewCommentUpdateRequest request) {
        ReviewComment comment = getOwnCommentOrThrow(commentId, currentUser.getUserId());
        commentMapper.updateEntity(comment, request);
        return commentMapper.toDto(comment, currentUser);
    }

    /**
     * 댓글 삭제 (Soft delete)
     */
    @Transactional
    public void deleteComment(Long commentId, UserInfo currentUser) {
        ReviewComment comment = getOwnCommentOrThrow(commentId, currentUser.getUserId());
        comment.deleteReviewComment(); // 도메인 메서드
    }

    /**
     * 댓글 좋아요 등록/해제
     */
    @Transactional
    public ReviewCommentLikeResponse updateLikeStatus(Long commentId, UserInfo currentUser, boolean isLiked) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        ReviewCommentLike like = likeRepository.findByReviewCommentIdAndUser(commentId, currentUser)
                .orElseGet(() -> commentMapper.toEntity(comment, currentUser, isLiked));

        like.updateLikeStatus(isLiked); // 도메인 메서드
        likeRepository.save(like);

        return ReviewCommentLikeResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(commentId)
                .userId(currentUser.getUserId())
                .isLiked(isLiked)
                .build();
    }

    /**
     * 좋아요 여부 조회
     */
    public boolean isLiked(Long commentId, UserInfo currentUser) {
        return likeRepository.findByReviewCommentIdAndUser(commentId, currentUser)
                .map(ReviewCommentLike::isLiked)
                .orElse(false);
    }

    /**
     * 댓글 수 조회
     */
    public Long getCommentCount(Long commentId) {
        return commentRepository.commentCount(commentId);
    }

    //댓글 좋아요 수 조회
    public Long getLikeCount(Long commentId) {
        return likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);
    }


    /**
     * 본인 댓글 확인
     */
    private ReviewComment getOwnCommentOrThrow(Long commentId, Long userId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new SecurityException("본인의 댓글만 수정 또는 삭제할 수 있습니다.");
        }
        return comment;
    }
}
