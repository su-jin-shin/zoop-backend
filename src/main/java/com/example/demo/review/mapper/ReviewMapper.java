package com.example.demo.review.mapper;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.enums.HasChildren;
import com.example.demo.review.domain.enums.IsResident;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.Review.Request.ReviewCreateRequest;
import com.example.demo.review.dto.Review.Response.ReviewCreateResponse;
import com.example.demo.review.dto.Review.Response.ReviewLikeResponse;
import com.example.demo.review.dto.Review.Response.ReviewListResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewMapper {

    /**
     * @ 리뷰 엔티티를 응답 DTO로 변환
     * @ 유저, 평점, 좋아요 등 포함
     */
    public ReviewCreateResponse toDto(Review review,
                                      long likeCount,
                                      long commentCount,
                                      boolean isLikedByMe,
                                      boolean isMine) {
        UserInfo user = review.getUser();  // 리뷰 작성자 (UserInfo)
        return ReviewCreateResponse.builder()
                .reviewId(review.getId())
                .userId(user != null ? user.getUserId() : null)  // 작성자 userId
                .nickname(user != null ? user.getNickname() : null)  // 작성자 닉네임
                .profileImage(user != null ? user.getProfileImage() : null)  // 작성자 프로필 이미지
                .rating(review.getRating())  // 평점
                .content(review.getContent())  // 리뷰 내용
                .hasChildren(review.getHasChildren())  //
                .isResident(review.getIsResident())    //
                .likeCount(likeCount)  // 좋아요 수
                .commentCount(commentCount)  // 댓글 수
                .isLikedByMe(isLikedByMe)  // 로그인한 유저가 좋아요를 눌렀는지 여부
                .isMine(isMine)  // 로그인한 유저가 작성한 리뷰인지 여부
                .createdAt(review.getCreatedAt())  // 작성 시간
                .updatedAt(review.getUpdatedAt())  // 수정 시간
                .build();
    }

    /**
     * @ 리뷰 리스트 응답 DTO로 변환
     * @ 페이지 정보와 평균 별점 포함
     */
    public ReviewListResponse toListResponse(Page<Review> page,
                                             List<ReviewCreateResponse> reviewCreateRespons,
                                             Long complexId,
                                             Long propertyId,
                                             BigDecimal avgRating) {
        return ReviewListResponse.builder()
                .complexId(complexId)  // 복합단지 ID
                .propertyId(propertyId)  // 매물 ID
                .avgRating(avgRating)
                .reviews(reviewCreateRespons)  // 리뷰 목록
                .page((long) page.getNumber())  // 현재 페이지 (int -> Long으로 형변환)
                .size((long)page.getSize())  // 페이지 크기
                .totalCount(page.getTotalElements())  // 전체 리뷰 개수
                .build();
    }


    /**
     * @ 리뷰 작성 요청을 리뷰 엔티티로 변환
     * @ 기본값 설정 포함
     */
    public Review toEntity(Long propertyId, ReviewCreateRequest request, UserInfo loginUser) {
        return Review.builder()
                .propertyId(propertyId)  // 매물 ID
                .user(loginUser)  // 로그인된 유저의 UserInfo 객체
                .rating(request.getRating() != null ? request.getRating() : BigDecimal.valueOf(0.5))  // 평점 (기본값: 0.5)
                .content(request.getContent() != null ? request.getContent() : "")  // 리뷰 내용 (기본값: "")
                .hasChildren(request.getHasChildren() != null ? request.getHasChildren() : HasChildren.NON_CHILDREN)
                .isResident(request.getIsResident() != null ? request.getIsResident() : IsResident.NON_RESIDENT)
                .likeCount(0L)  // 기본적으로 좋아요 수는 0
                .createdAt(LocalDateTime.now())  // 생성일
                .updatedAt(LocalDateTime.now())  // 수정일
                .build();
    }




    /**
     * @ 리뷰 좋아요 상태를 응답 DTO로 변환
     * @ null 방지용 기본값 포함
     */
    public ReviewLikeResponse likeResponse(Long reviewId, Long userId, Boolean isLiked, Long likeCount) {
        return ReviewLikeResponse.builder()
                .reviewId(reviewId != null ? reviewId : -1L)
                .userId(userId != null ? userId : -1L)
                .isLiked(isLiked != null && isLiked)
                .likeCount(likeCount != null ? likeCount : 0L)
                .build();
    }


}










