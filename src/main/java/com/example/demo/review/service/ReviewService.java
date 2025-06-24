/**
 * ReviewService
 *
 * 사용자 리뷰와 관련된 주요 기능을 담당하는 서비스 클래스입니다.
 *
 * 주요 기능:
 * - 리뷰 작성, 수정, 삭제 (Soft Delete 방식)
 * - 단지 또는 매물 기준 리뷰 목록 조회
 * - 리뷰 좋아요 등록 및 취소, 좋아요 여부 및 개수 조회
 *
 *  설계 포인트:
 * - 리뷰는 원칙적으로 단지 단위로 작성되도록 설계
 *   예: OO아파트(단지) → 의미 있는 피드백이 가능
 * - 단지 정보가 없는 매물도 일부 존재하여, fallback으로 매물 단위 조회 허용
 * - 좋아요는 중복 저장 방지를 위해 사용자 + 리뷰 조합 기준으로 처리
 */


package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.*;
import com.example.demo.property.domain.Property;
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


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewService {

    //============== 의존성 주입 =================
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;
    private final UserInfoRepository userInfoRepository;

    /*
     *   리뷰 전체 조회 (단지 or 매물 단위 분기 처리)
    */

    public ReviewListResponse getReviews(Long userId, Long propertyId) {
        // 1. 유저 및 매물 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        Property property = propertyRepository.findById(propertyId).orElseThrow(PropertyNotFoundException::new);

        // 2. complexId 유무에 따라 리뷰 조회 방식 분기
        Long complexId = property.getComplex() != null ? property.getComplex().getId() : null;

        // 정렬 조건 및 페이징 설정 (임시 고정값)
        String sort = "latest";
        int page = 0;
        int size = 10;

        Page<Review> reviewPage;

        // 단지 정보 유무에 따라 조회 방식 분기
        if (complexId != null) {
            // 같은 단지 내 모든 매물에 대한 리뷰 조회
            reviewPage = reviewRepository.findReviewsByComplexId(complexId, sort, page, size);
        } else {
            // 개별 매물 리뷰 조회
            reviewPage = reviewRepository.findReviewsByPropertyId(propertyId, sort, page, size);
        }

        // 3. 리뷰 관련 정보들 조회 : 좋아요 수, 댓글 수 , 좋아요 여부, 작성자 여부
        List<Review> reviews = reviewPage.getContent();
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        Map<Long, Long> likeCountMap = reviewLikeRepository.countLikesMap(reviewIds);
        Map<Long, Long> commentCountMap = reviewCommentRepository.countCommentsMap(reviewIds);
        Map<Long, Boolean> isLikedMap = reviewLikeRepository.getIsLikedMapByReviewIds(reviewIds, loginUser);

        // 4. 리뷰 DTO 변환
        List<ReviewCreateResponse> reviewCreateRespons = reviews.stream()
                .map(review -> {
                    Long reviewId = review.getId();
                    long likeCount = likeCountMap.getOrDefault(reviewId, 0L);
                    long commentCount = commentCountMap.getOrDefault(reviewId, 0L);
                    boolean isLiked = isLikedMap.getOrDefault(reviewId, false);
                    boolean isMyReview = review.getUser().getUserId().equals(loginUser.getUserId());

                    return reviewMapper.toDto(review, likeCount, commentCount, isLiked, isMyReview);
                })
                .toList();

        // 5. 평균 리뷰 별점 계산
        BigDecimal avgRating;
        if (complexId != null) {
            avgRating = reviewRepository.calculateAverageRatingByComplex(complexId);
        } else {
            avgRating = reviewRepository.calculateAverageRating(propertyId);
        }

        return reviewMapper.toListResponse(reviewPage, reviewCreateRespons, complexId, propertyId,avgRating);
    }


    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewCreateResponse createReview(Long propertyId, ReviewCreateRequest request, Long userId) {
        // 1. 유저 및 매물 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);


        Review review = reviewMapper.toEntity(propertyId, request, loginUser);
        reviewRepository.save(review);

        return reviewMapper.toDto(review, 0L, 0L, false, true); // 새 리뷰는 초기값 세팅
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewCreateResponse updateReview(Long reviewId, ReviewUpdateRequest request, Long userId) {
        // 1. 유저 및 리뷰 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        Review review = getReview(reviewId);

        // 2. 작성자 본인 확인
        if (!review.isMine(loginUser)) throw new UnauthorizedAccessException();


        // 3. 요청 필드가 null이 아닐 때만 업데이트
        if (request.getContent() != null) review.updateContent(request.getContent());
        if (request.getRating() != null) review.updateRating(request.getRating());
        if (request.getIsResident() != null) review.updateIsResident(request.getIsResident());
        if (request.getHasChildren() != null) review.updateHasChildren(request.getHasChildren());


        Long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
        Long commentCount = reviewCommentRepository.commentCount(reviewId);
        return reviewMapper.toDto(review, likeCount, commentCount, false, true);
    }

    /**
     * 리뷰 삭제 (soft delete)
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        // 1. 유저 및 리뷰 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        Review review = getReview(reviewId);

        // 2. 작성자 본인 확인
        if (!review.isMine(loginUser)) throw new UnauthorizedAccessException();

        review.deleteReview();
    }

    /**
     * 좋아요 등록/취소
     */

    @Transactional
    public ReviewLikeResponse updateLikeStatus(Long reviewId, boolean isLiked, Long userId) {
        // 1. 유저 및 리뷰 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        Review review = getReview(reviewId);


        // 2. 기존 좋아요가 있는지 확인 후 없으면 생성, 있으면 상태 변경
        ReviewLike like = reviewLikeRepository.findByReviewIdAndUser(reviewId, loginUser)
                .orElseGet(() -> {
                    ReviewLike newLike = ReviewLike.of(review, loginUser, isLiked);
                    reviewLikeRepository.save(newLike);
                    return newLike;
                });

        like.updateLikeStatus(isLiked);

        long likeCount = reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);

        // 3. Mapper 위임
        return reviewMapper.likeResponse(reviewId, loginUser.getUserId(), isLiked, likeCount);
    }



    /**
     * 좋아요 여부 조회
     */
    public ReviewLikeResponse getLikeStatus(Long reviewId, Long userId) {
        // 1. 유저 및 리뷰 유효성 검사
        UserInfo loginUser = getUserInfo(userId);
        getReview(reviewId);

        // 2. 좋아요 여부 조회
        boolean isLiked = reviewLikeRepository.findByReviewIdAndUser(reviewId, loginUser)
                .map(ReviewLike::isLiked)
                .orElse(false);

        // DTO로 리턴
        return ReviewLikeResponse.builder()
                .reviewId(reviewId)
                .userId(userId)
                .isLiked(isLiked)
                .build();
    }




    /**
     * 좋아요 수 조회 
     */
    public Long getLikeCount(Long reviewId) {
        getReview(reviewId); // 리뷰 유효성 검증
        return reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
    }

    /**
     * 댓글 개수
     */
    public Long getCommentCount(Long reviewId) {
        getReview(reviewId);  // 리뷰 유효성 검증
        return reviewCommentRepository.commentCount(reviewId);
    }

    //============== 공통 메서드 ===============

    // 1. 리뷰 검증 및 가져오기
    private Review getReview(Long reviewId) {
        // 등록 된 적 없는 리뷰 요청인지 확인
        reviewRepository.findReviewById(reviewId).orElseThrow(NotFoundException::new);
        // 삭제되지 않은 리뷰인지 검증
        Review review = reviewRepository.findActiveById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return review;
    }

    // 2. 로그인한 유저 검증 및 가져오기
    private UserInfo getUserInfo(Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        return loginUser;
    }


}



