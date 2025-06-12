package com.example.demo.review.mapper;

//Review + UserInfo + 로그인유저 정보+ (commentCount, isLikedByMe 등의 기타 정보 )
//--> ReviewResponse로 변환 하는 정적 메서드
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewLike;
import com.example.demo.review.dto.Review.ReviewCreateRequest;
import com.example.demo.review.dto.Review.ReviewResponse;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.repository.ReviewCommentRepository;
import com.example.demo.review.repository.ReviewLikeRepository;
import com.example.demo.review.dto.Review.ReviewListResponse;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final ReviewLikeRepository reviewLikeRepo;
    private final ReviewCommentRepository reviewCommentRepo;



    // Entity -> DTO (단건)
    public ReviewResponse toDto(Review review, UserInfo currentUser) {

        // 좋아요 개수
        long likeCount = reviewLikeRepo.countByReviewIdAndIsLikedTrue(review.getId());

        // 댓글 개수
        long commentCount = reviewCommentRepo.commentCount(review.getId());

        // 로그인한 유저가 좋아요 눌렀는지
        boolean isLiked = reviewLikeRepo.findByReviewIdAndUser(review.getId(), currentUser)
                .map(ReviewLike::isLiked)
                .orElse(false);

        // 본인 리뷰 여부
        boolean isMine = review.isMine(currentUser);

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getUserId())
                .nickname(review.getUser().getNickname())
                .profileImage(review.getUser().getProfileImage())
                .rating(review.getRating())
                .content(review.getContent())
                .isResident(review.isResident())
                .hasChildren(review.isHasChildren())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isLikedByMe(isLiked)
                .isMine(isMine)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }




    // Entity -> DTO (리스트)
    public ReviewListResponse toListResponse(
            Page<Review> reviewPage,
            Long complexId,
            Long propertyId,
            UserInfo currentUser
    ) {
        List<ReviewResponse> responses = reviewPage.getContent().stream()
                .map(review -> toDto(review, currentUser))
                .toList();

        return ReviewListResponse.builder()
                .complexId(complexId)
                .propertyId(propertyId)
                .reviews(responses)
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalCount(reviewPage.getTotalElements())
                .build();
    }


    // DTO -> Entity
    public Review toEntity(Long propertyId, ReviewCreateRequest request, UserInfo currentUser) {
        return Review.builder()
                .propertyId(propertyId)
                .user(currentUser)
                .rating(request.getRating())
                .content(request.getContent())
                .hasChildren(request.isHasChildren())
                .isResident(request.isResident())
                .likeCount(0L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //DTO -> Entity
    public ReviewLike toEntity(Review review, UserInfo currentUser, boolean isLiked) {
        return ReviewLike.builder()
                .review(review)
                .user(currentUser)
                .isLiked(isLiked)
                .build();
    }

}
