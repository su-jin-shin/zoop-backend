package com.example.demo.mypage.service;

import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.repository.MyReviewCommentRepository;
import com.example.demo.property.service.PropertyService;
import com.example.demo.review.domain.ReviewComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyCommentService {

    private final MyReviewCommentRepository reviewCommentRepository;
    private final PropertyService propertyService;

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
    }
    public List<MyCommentResponse> getMyComments(Long userId) {
        validateUserId(userId);
        List<ReviewComment> comments = reviewCommentRepository.findActiveByUserWithAliveReview(userId);
        return comments.stream().map(this::convertToDto).toList();
    }

    private MyCommentResponse convertToDto(ReviewComment comment) {
        Long complexId = null;
        Long propertyId = null;
        String articleName = "매물 정보 없음";

        var review = comment.getReview();
        if (review == null) {
            return new MyCommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedAt().toLocalDate(),
                    comment.getLikeCount().intValue(),
                    null // 리뷰 정보 없음
            );
        }

        if (review.getComplex() != null) {
            complexId = review.getComplex().getId();
            articleName = review.getComplex().getComplexName();
        } else if (review.getPropertyId() != null) {
            propertyId = review.getPropertyId();
            articleName = Optional.ofNullable(propertyService.getPropertyBasicInfo(propertyId))
                    .map(info -> info.getArticleName())
                    .orElse("매물 정보 없음");
        }

        MyCommentResponse.Item item = new MyCommentResponse.Item(
                complexId,
                propertyId,
                articleName
        );

        MyCommentResponse.Review reviewDto = new MyCommentResponse.Review(
                comment.getReview().getId(),
                comment.getReview().getContent(),
                item
        );
        return new MyCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toLocalDate(),
                comment.getLikeCount().intValue(),
                reviewDto
        );
    }
}