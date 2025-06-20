package com.example.demo.mypage.service;

import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.repository.MyReviewCommentRepository;
import com.example.demo.property.service.PropertyService;
import com.example.demo.review.domain.ReviewComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyCommentService {

    private final MyReviewCommentRepository reviewCommentRepository;
    private final PropertyService propertyService;

    public List<MyCommentResponse> getMyComments(Long userId) {
        log.info("💬 MyCommentService 진입 userId={}", userId);

        List<ReviewComment> comments = reviewCommentRepository.findByUser_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
        log.info("💬 댓글 수 = {}", comments.size());

        return comments.stream().map(this::convertToDto).toList();
    }

    private MyCommentResponse convertToDto(ReviewComment comment) {
        Long complexId = null;
        Long propertyId = null;
        String articleName = "매물 정보 없음";

        if (comment.getReview() != null) {
            if (comment.getReview().getComplex() != null) {
                complexId = comment.getReview().getComplex().getId();
                articleName = comment.getReview().getComplex().getComplexName();
            } else if (comment.getReview().getPropertyId() != null) {
                propertyId = comment.getReview().getPropertyId();
                articleName = propertyService.getPropertyBasicInfo(propertyId).getArticleName();
            }
        }

        MyCommentResponse.Item item = new MyCommentResponse.Item(
                complexId,
                propertyId,
                articleName
        );

        MyCommentResponse.Review review = new MyCommentResponse.Review(
                comment.getReview().getId(),
                comment.getReview().getContent(),
                item
        );

        return new MyCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toLocalDate(),
                comment.getLikeCount().intValue(),
                review
        );
    }
}