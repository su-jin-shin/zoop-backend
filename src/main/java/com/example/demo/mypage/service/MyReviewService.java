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
        List<Review> reviews = myReviewRepository.findByUserUserId(userId);
        log.info("✅ 조회된 리뷰 수: {}", reviews.size());
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
        log.debug("🧪 reviewIds: {}", reviewIds);

        Map<Long, Boolean> isLikedMap = myReviewRepository.getIsLikedMapByReviewIds(reviewIds, userId);

        return reviews.stream().map(review -> {
            long commentCount = myReviewRepository.countByReviewId(review.getId());
            long likeCount = myReviewRepository.countByReviewIdAndIsLikedTrue(review.getId());
            boolean isLiked = isLikedMap.getOrDefault(review.getId(), false);

            String articleName = "매물 정보 없음";
            Long propertyId = null;

            if (review.getComplex() != null) {
                articleName = review.getComplex().getComplexName();
            } else if (review.getPropertyId() != null) {
                try {
                    articleName = propertyService.getPropertyBasicInfo(review.getPropertyId()).getArticleName();
                    propertyId = review.getPropertyId();
                } catch (NotFoundException e) {
                    log.warn("⚠️ 매물 정보 없음 - propertyId={}", review.getPropertyId());
                }
            }

            return MyReviewResponse.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt().toLocalDate())
                    .likeCount((int) likeCount)
                    .commentCount((int) commentCount)
                    .isLiked(isLiked)
                    .item(MyReviewResponse.ItemDto.builder()
                            .propertyId(propertyId)
                            .articleName(articleName)
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }
}