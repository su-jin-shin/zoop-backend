/*
1. 리뷰 작성, 수정, 삭제
2. 리스트 조회
3. 좋아요 등록/취소
4. 좋아요 여부 및 개수 조회
 */


package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.exception.ReviewNotFoundException;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.domain.*;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.mapper.ReviewMapper;
import com.example.demo.review.repository.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;
    private final UserInfoRepository userInfoRepository;


    public ReviewListResponse getReviews(Long userId, Long propertyId) {
        // 사용자 검증 : userId로 UserInfo 조회
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리

        // 매물 존재 여부 확인 (propertyId로 매물 조회)
        propertyRepository.findById(propertyId)
                .orElseThrow(NotFoundException::new);  // 매물이 없으면 예외 처리


        // 해당 propertyId에 대한 리뷰 목록 조회
        var reviewPage = reviewRepository.findReviewsByPropertyId(propertyId, "like", 0, 10); // propertyId로 리뷰 조회

        List<Review> reviews = reviewPage.getContent();
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        // 좋아요, 댓글 개수 미리 조회 (N+1 방지)
        Map<Long, Long> likeCountMap = reviewLikeRepository.countLikesMap(reviewIds);
        Map<Long, Long> commentCountMap = reviewCommentRepository.countCommentsMap(reviewIds);

        // 로그인한 경우 좋아요 여부 미리 조회
        Map<Long, Boolean> isLikedMap = reviewLikeRepository.getIsLikedMapByReviewIds(reviewIds, loginUser);

        List<ReviewCreateResponse> reviewCreateRespons = reviews.stream().map(review -> {
            Long reviewId = review.getId();

            long likeCount = likeCountMap.getOrDefault(reviewId, 0L);
            long commentCount = commentCountMap.getOrDefault(reviewId, 0L);
            boolean isLiked = isLikedMap.getOrDefault(reviewId, false);
            boolean isMyReview = review.getUser().getUserId().equals(loginUser.getUserId());

            return reviewMapper.toDto(review, likeCount, commentCount, isLiked, isMyReview);
        }).toList();

        return reviewMapper.toListResponse(reviewPage, reviewCreateRespons, null, propertyId); // complexId는 null로 처리
    }


    /**
     * 리뷰 생성
     */
    @Transactional
    public ReviewCreateResponse createReview(Long propertyId, ReviewCreateRequest request, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리


        Review review = reviewMapper.toEntity(propertyId, request, loginUser);
        reviewRepository.save(review);

        return reviewMapper.toDto(review, 0L, 0L, false, true); // 새 리뷰는 초기값 세팅
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewCreateResponse updateReview(Long reviewId, ReviewUpdateRequest request, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리

        Review review = reviewRepository.findActiveById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
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
    public void deleteReview(Long reviewId, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리


        Review review = reviewRepository.findActiveById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        if (!review.isMine(loginUser)) throw new UnauthorizedAccessException();

        review.deleteReview();
    }

    /**
     * 좋아요 등록/해제
     */
    @Transactional
    public ReviewLikeResponse updateLikeStatus(Long reviewId, boolean isLiked, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리


        Review review = reviewRepository.findActiveById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        ReviewLike like = reviewLikeRepository.findByReviewIdAndUser(reviewId, loginUser)
                .orElseGet(() -> {
                    ReviewLike newLike = ReviewLike.of(review, loginUser, isLiked);
                    reviewLikeRepository.save(newLike); // 새로 만들었을 때만 저장
                    return newLike;
                });

        like.updateLikeStatus(isLiked); // 값이 바뀌었을 때만 내부적으로 시간 변경됨

        return ReviewLikeResponse.builder()
                .reviewId(reviewId)
                .userId(loginUser.getUserId())
                .isLiked(isLiked)
                .build();
    }

    /**
     * 좋아요 여부
     */
    public boolean isLiked(Long reviewId, Long userId) {

        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리

        return reviewLikeRepository.findByReviewIdAndUser(reviewId, loginUser)
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


    //내 리뷰 조회
    public List<ReviewCreateResponse> getMyReviews(Long userId) {
        UserInfo user = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Review> reviews = reviewRepository.findActiveByUser(user);  // deletedAt IS NULL 조건 포함된 메서드 사용

        return reviews.stream()
                .map(review -> {
                    long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(review.getId());
                    long commentCount = reviewCommentRepository.commentCount(review.getId());
                    return reviewMapper.toDto(review, likeCount, commentCount, true, true);
                })
                .toList();
    }


}



