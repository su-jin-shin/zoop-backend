package com.example.demo.review.dto.Review.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

// 리뷰 좋아요 등록/취소 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewLikeResponse {
    private Long reviewId;
    private Long userId;
    private Boolean isLiked;
    private Long likeCount;
}

