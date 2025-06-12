package com.example.demo.mypage.service;

import com.example.demo.mypage.dto.MyReviewResponse;
import com.example.demo.mypage.repository.MyReviewRepository;
import com.example.demo.property.service.PropertyService;
import com.example.demo.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyReviewService {

    private final MyReviewRepository myReviewRepository;
    private final PropertyService propertyService;

    public List<MyReviewResponse> getMyReviews(Long userId) {
        List<Review> reviews = myReviewRepository.findByUserUserId(userId);

        return reviews.stream().map(review -> {
            int commentCount = myReviewRepository.countByReviewId(review.getId());

            String articleName = "매물 정보 없음";
            Long propertyId = null;

            // Complex 엔티티에 @OneToMany(mappedBy = "complex", fetch = FetchType.LAZY)
            //private List<Property> properties; 필드가 정의되어야 함
            if (review.getComplex() != null /*&& !review.getComplex().getProperties().isEmpty()*/) {
                articleName = review.getComplex().getComplexName();
//                propertyId = review.getComplex().getProperties().get(0).getId();
            } else if (review.getPropertyId() != null) {
                articleName = propertyService.getPropertyBasicInfo(review.getPropertyId()).getArticleName();
                propertyId = review.getPropertyId();
            }

            return MyReviewResponse.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt().toLocalDate())
                    .likeCount(review.getLikeCount().intValue())
                    .commentCount(commentCount)
                    .item(MyReviewResponse.ItemDto.builder()
                            .propertyId(propertyId)
                            .articleName(articleName)
                            .build())
                    .build();
        }).collect(Collectors.toList());

    }
}
