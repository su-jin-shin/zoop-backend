package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.MyReviewResponse;
import com.example.demo.mypage.repository.MyReviewRepository;
import com.example.demo.property.service.PropertyService;
import com.example.demo.review.domain.Review;
import com.example.demo.review.repository.ReviewCommentRepository;
import com.example.demo.review.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyReviewService {

    private final MyReviewRepository myReviewRepository;
    private final PropertyService propertyService;

    public List<MyReviewResponse> getMyReviews(Long userId) {
        log.info("🔍 getMyReviews() 시작 - userId: {}", userId);
        List<Review> reviews = myReviewRepository.findByUserUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
        log.info("✅ 조회된 리뷰 수: {}", reviews.size());

        return reviews.stream().map(review -> {
            long commentCount = myReviewRepository.countCommentsByReviewId(review.getId());
            long likeCount = myReviewRepository.countByReviewIdAndIsLikedTrue(review.getId());

            String articleName = "매물 정보 없음";
            Long propertyId = null;

            if (review.getComplex() != null) {
                articleName = review.getComplex().getComplexName();
            } else if (review.getPropertyId() != null) {
                propertyId = review.getPropertyId();
                try {
                    articleName = propertyService.getPropertyBasicInfo(propertyId).getArticleName();
                } catch (NotFoundException e) {
                    log.warn("⚠️ 매물 정보 없음 - propertyId={}", propertyId);
                }
            }

            return MyReviewResponse.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt().toLocalDate())
                    .likeCount((int) likeCount)
                    .commentCount((int) commentCount)
                    .item(MyReviewResponse.ItemDto.builder()
                            .propertyId(propertyId)
                            .articleName(articleName)
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }
}