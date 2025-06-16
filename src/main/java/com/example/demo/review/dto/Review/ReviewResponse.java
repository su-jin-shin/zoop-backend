package com.example.demo.review.dto.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//개별 리뷰 정보 DTO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewResponse {
    private Long reviewId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private BigDecimal rating;
    private String content;
    private Boolean hasChildren;
    private Boolean isResident;
    private long likeCount;
    private long commentCount;
    private Boolean isLikedByMe;
    private Boolean isMine;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
