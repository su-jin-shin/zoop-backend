package com.example.demo.review.dto.Review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewLikeResponse {
    private Long reviewId;
    private Long userId;
    private boolean isLiked;
}
