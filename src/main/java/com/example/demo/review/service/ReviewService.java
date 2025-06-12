/*
1. 리뷰 작성, 수정, 삭제
2. 리스트 조회
3. 좋아요 등록/취소
4. 좋아요 여부 및 개수 조회
 */



package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.*;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.mapper.*;
import com.example.demo.review.repository.*;
import jakarta.persistence.EntityNotFoundException;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewMapper reviewMapper;


    /**
     * 리뷰 단일 조회
     */
    public ReviewResponse getReviewById(Long reviewId, UserInfo currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
        return reviewMapper.toDto(review, currentUser);
    }

    /**
     * 리뷰 목록 조회 (정렬/필터/페이징 지원)
     */
    public ReviewListResponse getReviews(
            Long complexId,
            Long propertyId,
            String sort,
            boolean isMine,
            UserInfo currentUser,
            int page,
            int size
    ) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };

        Pageable pageable = PageRequest.of(page, size, sortOption);
        Page<Review> reviewPage;

        if (isMine) {
            reviewPage = reviewRepository.findByUser(currentUser, pageable);
        } else if (complexId != null) {
            reviewPage = reviewRepository.findByComplexId(complexId, pageable);
        } else if (propertyId != null) {
            reviewPage = reviewRepository.findByPropertyId(propertyId, pageable);
        } else {
            throw new IllegalArgumentException("complexId 또는 propertyId는 필수입니다.");
        }


        return reviewMapper.toListResponse(reviewPage, complexId, propertyId, currentUser);
    }






/*----------------------------------------------------------------------------------*/
    // 리뷰 작성
    @Transactional
    public ReviewResponse createReview(Long propertyId, ReviewCreateRequest request, UserInfo currentUser) {
        Review review = reviewMapper.toEntity(propertyId, request, currentUser);
        reviewRepository.save(review); //
        return reviewMapper.toDto(review, currentUser);
    }


// 리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request, UserInfo currentUser) {

        //1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));


        //2. 작성자 본인 확인

        if(!review.isMine(currentUser)){
            throw new SecurityException("본인의 리뷰만 수정할 수 있습니다.");
        }

        //3. 리뷰 수정 - 변경사항만 반영 (내용, 별점만 수정 가능)
        if(request.getContent() != null){
            review.updateContent(request.getContent());
        }

        if(request.getRating() != null){
            review.updateRating(request.getRating());
        }

        //4. DTO 변환(부가 정보 포함)
        return reviewMapper.toDto(review, currentUser);
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, UserInfo currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        if (!review.isMine(currentUser)) {
            throw new SecurityException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        review.deleteReview(); //도메인의 비즈니스메서드 사용
    }




    // 좋아요 등록/해제
    @Transactional
    public ReviewLikeResponse updateLikeStatus(Long reviewId, boolean isLiked, UserInfo currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        ReviewLike like = reviewLikeRepository.findByReviewIdAndUser(reviewId, currentUser)
                .orElseGet(() -> reviewMapper.toEntity(review, currentUser, isLiked));

        like.updateLikeStatus(isLiked);
        reviewLikeRepository.save(like);

        return ReviewLikeResponse.builder()
                .reviewId(reviewId)
                .userId(currentUser.getUserId())
                .isLiked(like.isLiked())
                .build();
    }



    // 좋아요 여부 조회
    public boolean isLiked(Long reviewId, UserInfo currentUser) {
        return reviewLikeRepository.findByReviewIdAndUser(reviewId, currentUser)
                .map(ReviewLike::isLiked)
                .orElse(false);
    }

    // 좋아요 개수 조회
    public Long getLikeCount(Long reviewId) {
        return reviewLikeRepository.countByReviewIdAndIsLikedTrue(reviewId);
    }

    // 댓글 개수 조회
    public Long getCommentCount(Long reviewId) {
        return reviewCommentRepository.commentCount(reviewId);
    }
}
