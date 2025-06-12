package com.example.demo.review.dto.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//개별 리뷰 정보 DTO
@Data
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
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
