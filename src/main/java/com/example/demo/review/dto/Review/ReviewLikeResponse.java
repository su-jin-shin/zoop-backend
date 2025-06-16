package com.example.demo.review.dto.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewLikeResponse {
    private Long reviewId;
    private Long userId;
    private Boolean isLiked;
}

