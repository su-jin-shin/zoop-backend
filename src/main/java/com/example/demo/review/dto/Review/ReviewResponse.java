package com.example.demo.review.dto.Review;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//개별 리뷰 정보 DTO
@Data
@Builder
public class ReviewResponse {
    private Long reviewId;

    private Long userId;
    private String nickname;
    private String profileImage;

    private BigDecimal rating;
    private String content;
    private boolean hasChildren;
    private boolean isResident;

    private long likeCount;
    private long commentCount;  //별도 계산 로직 필요
    private boolean isLikedByMe;  //로그인 유저 기반 Service 단 판단 필요
    private boolean isMine;     //로그인 유저 기반 Service 단 판단 필요

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
