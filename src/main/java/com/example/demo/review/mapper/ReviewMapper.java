package com.example.demo.review.mapper;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewLike;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.repository.ReviewLikeRepository;
import com.example.demo.review.repository.ReviewCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final ReviewLikeRepository reviewLikeRepo;
    private final ReviewCommentRepository reviewCommentRepo;

    public ReviewResponse toDto(Review review, long likeCount, long commentCount, boolean isLikedByMe, boolean isMine) {
        UserInfo user = review.getUser();
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .userId(user != null ? user.getUserId() : null)
                .nickname(user != null ? user.getNickname() : null)
                .profileImage(user != null ? user.getProfileImage() : null)
                .rating(review.getRating())
                .content(review.getContent())
                .hasChildren(review.isHasChildren())
                .isResident(review.isResident())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isLikedByMe(isLikedByMe)
                .isMine(isMine)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public ReviewListResponse toListResponse(Page<Review> page, List<ReviewResponse> reviewResponses, Long complexId, Long propertyId) {
        return ReviewListResponse.builder()
                .complexId(complexId)
                .propertyId(propertyId)
                .reviews(reviewResponses)
                .page(page.getNumber())
                .size(page.getSize())
                .totalCount(page.getTotalElements())
                .build();
    }

    public Review toEntity(Long propertyId, ReviewCreateRequest request, LoginUser loginUser) {
        return Review.builder()
                .propertyId(propertyId)
                .user(loginUser.getUserInfo())
                .rating(request.getRating() != null ? request.getRating() : BigDecimal.valueOf(0.5))
                .content(request.getContent() != null ? request.getContent() : "")
                .hasChildren(Boolean.TRUE.equals(request.getHasChildren()))
                .isResident(Boolean.TRUE.equals(request.getIsResident()))
                .likeCount(0L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ReviewLike toEntity(Review review, LoginUser loginUser, boolean isLiked) {
        return ReviewLike.builder()
                .review(review)
                .user(loginUser.getUserInfo())
                .isLiked(isLiked)
                .build();
    }
}








