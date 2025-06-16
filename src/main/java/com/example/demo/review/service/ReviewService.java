/*
1. 리뷰 작성, 수정, 삭제
2. 리스트 조회
3. 좋아요 등록/취소
4. 좋아요 여부 및 개수 조회
 */


package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.domain.*;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.mapper.ReviewMapper;
import com.example.demo.review.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;

    /**
     * 리뷰 단건 조회
     */
    public ReviewResponse getReviewById(Long reviewId, LoginUser loginUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);

        Long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
        Long commentCount = reviewCommentRepository.commentCount(reviewId);
        UserInfo currentUser = loginUser != null ? loginUser.getUserInfo() : null;

        boolean isLiked = currentUser != null &&
                reviewLikeRepository.findByReviewIdAndUser(reviewId, currentUser)
                        .map(ReviewLike::isLiked).orElse(false);
        boolean isMine = currentUser != null &&
                review.getUser().getUserId().equals(currentUser.getUserId());

        return reviewMapper.toDto(review, likeCount, commentCount, isLiked, isMine);
    }

    /**
     * 리뷰 리스트 조회
     */
    public ReviewListResponse getReviews(Long complexId, Long propertyId, String sort,
                                         boolean isMine, LoginUser loginUser, int page, int size) {
        var reviewPage = reviewRepository.findPagedReviews(complexId, propertyId, sort, isMine, loginUser, page, size);
        var currentUser = loginUser != null ? loginUser.getUserInfo() : null;

        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream().map(review -> {
            Long reviewId = review.getId();
            long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
            long commentCount = reviewCommentRepository.commentCount(reviewId);
            boolean isLiked = currentUser != null &&
                    reviewLikeRepository.findByReviewIdAndUser(reviewId, currentUser)
                            .map(ReviewLike::isLiked).orElse(false);
            boolean isMineReview = currentUser != null &&
                    review.getUser().getUserId().equals(currentUser.getUserId());

            return reviewMapper.toDto(review, likeCount, commentCount, isLiked, isMineReview);
        }).toList();

        return reviewMapper.toListResponse(reviewPage, reviewResponses, complexId, propertyId);
    }

    /**
     * 리뷰 생성
     */
    @Transactional
    public ReviewResponse createReview(Long propertyId, ReviewCreateRequest request, LoginUser loginUser) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new NotFoundException();
        }

        Review review = reviewMapper.toEntity(propertyId, request, loginUser);
        reviewRepository.save(review);

        return reviewMapper.toDto(review, 0L, 0L, false, true); // 새 리뷰는 초기값 세팅
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request, LoginUser loginUser) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        if (!review.isMine(loginUser)) throw new UnauthorizedAccessException();

        if (request.getContent() != null) {
            review.updateContent(request.getContent());
        }
        if (request.getRating() != null) {
            review.updateRating(request.getRating());
        }

        Long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
        Long commentCount = reviewCommentRepository.commentCount(reviewId);
        return reviewMapper.toDto(review, likeCount, commentCount, false, true);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, LoginUser loginUser) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        if (!review.isMine(loginUser)) throw new UnauthorizedAccessException();

        review.deleteReview();
    }

    /**
     * 좋아요 등록/해제
     */
    @Transactional
    public ReviewLikeResponse updateLikeStatus(Long reviewId, boolean isLiked, LoginUser loginUser) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        UserInfo user = loginUser.getUserInfo();

        ReviewLike like = reviewLikeRepository.findByReviewIdAndUser(reviewId, user)
                .orElseGet(() -> reviewMapper.toEntity(review, loginUser, isLiked));

        like.updateLikeStatus(isLiked);
        reviewLikeRepository.save(like);

        return ReviewLikeResponse.builder()
                .reviewId(reviewId)
                .userId(user.getUserId())
                .isLiked(isLiked)
                .build();
    }

    /**
     * 좋아요 여부
     */
    public boolean isLiked(Long reviewId, LoginUser loginUser) {
        return reviewLikeRepository.findByReviewIdAndUser(reviewId, loginUser.getUserInfo())
                .map(ReviewLike::isLiked)
                .orElse(false);
    }

    /**
     * 좋아요 개수
     */
    public Long getLikeCount(Long reviewId) {
        return reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
    }

    /**
     * 댓글 개수
     */
    public Long getCommentCount(Long reviewId) {
        return reviewCommentRepository.commentCount(reviewId);
    }
}



