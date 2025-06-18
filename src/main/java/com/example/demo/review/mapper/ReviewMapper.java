package com.example.demo.review.mapper;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.HasChildren;
import com.example.demo.review.domain.IsResident;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewLike;
import com.example.demo.review.dto.Review.*;
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

    // Review 객체를 ReviewCreateResponse DTO로 변환
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

    // Review 객체 목록을 ReviewListResponse DTO로 변환
    public ReviewListResponse toListResponse(Page<Review> page,
                                             List<ReviewCreateResponse> reviewCreateRespons,
                                             Long complexId,
                                             Long propertyId) {
        return ReviewListResponse.builder()
                .complexId(complexId)  // 복합단지 ID
                .propertyId(propertyId)  // 매물 ID
                .reviews(reviewCreateRespons)  // 리뷰 목록
                .page((long) page.getNumber())  // 현재 페이지 (int -> Long으로 형변환)
                .size((long)page.getSize())  // 페이지 크기
                .totalCount(page.getTotalElements())  // 전체 리뷰 개수
                .build();
    }


    // ReviewCreateRequest를 받아 Review 엔티티로 변환
    public Review toEntity(Long propertyId, ReviewCreateRequest request, UserInfo loginUser) {
        return Review.builder()
                .propertyId(propertyId)  // 매물 ID
                .user(loginUser)  // 로그인된 유저의 UserInfo 객체
                .rating(request.getRating() != null ? request.getRating() : BigDecimal.valueOf(0.5))  // 평점 (기본값: 0.5)
                .content(request.getContent() != null ? request.getContent() : "")  // 리뷰 내용 (기본값: "")
                .hasChildren(request.getHasChildren() != null ? request.getHasChildren() : HasChildren.NO_CHILDREN)
                .isResident(request.getIsResident() != null ? request.getIsResident() : IsResident.NON_RESIDENT)
                .likeCount(0L)  // 기본적으로 좋아요 수는 0
                .createdAt(LocalDateTime.now())  // 생성일
                .updatedAt(LocalDateTime.now())  // 수정일
                .build();
    }

    // Review 객체와 로그인된 유저 정보를 받아 ReviewLike 엔티티로 변환
    public ReviewLike toEntity(Review review, UserInfo loginUser, boolean isLiked) {
        return ReviewLike.builder()
                .review(review)  // 해당 리뷰
                .user(loginUser)  // 로그인된 유저의 UserInfo 객체
                .isLiked(isLiked)  // 좋아요 여부
                .build();
    }
}










//package com.example.demo.review.mapper;
//
//import com.example.demo.auth.dto.LoginUser;
//import com.example.demo.auth.domain.UserInfo;
//import com.example.demo.review.domain.Review;
//import com.example.demo.review.domain.ReviewLike;
//import com.example.demo.review.dto.Review.*;
//
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
//public class ReviewMapper {
//
//    public ReviewCreateResponse toDto(Review review,
//                                      long likeCount,
//                                      long commentCount,
//                                      boolean isLikedByMe,
//                                      boolean isMine) {
//        UserInfo user = review.getUser();
//        return ReviewCreateResponse.builder()
//                .reviewId(review.getId())
//                .userId(user != null ? user.getUserId() : null)
//                .nickname(user != null ? user.getNickname() : null)
//                .profileImage(user != null ? user.getProfileImage() : null)
//                .rating(review.getRating())
//                .content(review.getContent())
//                .hasChildren(review.isHasChildren())
//                .isResident(review.isResident())
//                .likeCount(likeCount)
//                .commentCount(commentCount)
//                .isLikedByMe(isLikedByMe)
//                .isMine(isMine)
//                .createdAt(review.getCreatedAt())
//                .updatedAt(review.getUpdatedAt())
//                .build();
//    }
//
//    public ReviewListResponse toListResponse(Page<Review> page,
//                                             List<ReviewCreateResponse> reviewCreateRespons,
//                                             Long complexId,
//                                             Long propertyId) {
//        return ReviewListResponse.builder()
//                .complexId(complexId)
//                .propertyId(propertyId)
//                .reviews(reviewCreateRespons)
//                .page(page.getNumber())
//                .size(page.getSize())
//                .totalCount(page.getTotalElements())
//                .build();
//    }
//
//    public Review toEntity(Long propertyId, ReviewCreateRequest request, LoginUser loginUser) {
//        return Review.builder()
//                .propertyId(propertyId)
//                .user(loginUser.getUserInfo())
//                .rating(request.getRating() != null ? request.getRating() : BigDecimal.valueOf(0.5))
//                .content(request.getContent() != null ? request.getContent() : "")
//                .hasChildren(Boolean.TRUE.equals(request.getHasChildren()))
//                .isResident(Boolean.TRUE.equals(request.getIsResident()))
//                .likeCount(0L)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//    }
//
//    public ReviewLike toEntity(Review review, LoginUser loginUser, boolean isLiked) {
//        return ReviewLike.builder()
//                .review(review)
//                .user(loginUser.getUserInfo())
//                .isLiked(isLiked)
//                .build();
//    }
//}


