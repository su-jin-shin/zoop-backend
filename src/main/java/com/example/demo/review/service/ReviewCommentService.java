package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.mapper.ReviewCommentMapper;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import com.example.demo.review.repository.ReviewCommentRepository;
import com.example.demo.review.repository.ReviewRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    private final PropertyRepository propertyRepository;

    public List<ReviewCommentResponse> getComments(Long reviewId, LoginUser loginUser) {
        return commentRepository.findByReviewId(reviewId).stream()
                .map(comment -> commentMapper.toDto(comment, loginUser))
                .toList();
    }

    @Transactional
    public ReviewCommentResponse createComment(Long reviewId, LoginUser loginUser, ReviewCommentCreateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException());

        Long propertyId = review.getPropertyId();
        if (propertyId == null || !propertyRepository.existsById(propertyId)) {
            throw new NotFoundException();
        }

        ReviewComment comment = commentMapper.toEntity(request, loginUser, review);
        commentRepository.save(comment);

        return commentMapper.toDto(comment, loginUser);
    }

    @Transactional
    public ReviewCommentResponse updateComment(Long commentId, LoginUser loginUser, ReviewCommentUpdateRequest request) {
        ReviewComment comment = getOwnCommentOrThrow(commentId, loginUser.getUserId());

        comment.updateContent(request.getContent());
        commentRepository.save(comment);

        return commentMapper.toDto(comment, loginUser);
    }

    @Transactional
    public void deleteComment(Long commentId, LoginUser loginUser) {
        ReviewComment comment = getOwnCommentOrThrow(commentId, loginUser.getUserId());
        comment.deleteReviewComment();
    }

    @Transactional
    public ReviewCommentLikeResponse updateLikeStatus(Long commentId, LoginUser loginUser, boolean isLiked) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException());

        UserInfo userInfo = loginUser.getUserInfo();

        ReviewCommentLike like = likeRepository.findByReviewCommentIdAndUser(commentId, userInfo)
                .orElseGet(() -> commentMapper.toEntity(comment, userInfo, isLiked));

        like.updateLikeStatus(isLiked);
        likeRepository.save(like);

        return ReviewCommentLikeResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(commentId)
                .userId(userInfo.getUserId())
                .isLiked(isLiked)
                .build();
    }

    public boolean isLiked(Long commentId, LoginUser loginUser) {
        return likeRepository.findByReviewCommentIdAndUser(commentId, loginUser.getUserInfo())
                .map(ReviewCommentLike::isLiked)
                .orElse(false);
    }

    public Long getCommentCount(Long reviewId) {
        return commentRepository.commentCount(reviewId);
    }

    public Long getLikeCount(Long commentId) {
        return likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);
    }

    // 유저 본인의 댓글인지 확인
    private ReviewComment getOwnCommentOrThrow(Long commentId, Long userId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException());
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }
        return comment;
    }
}


