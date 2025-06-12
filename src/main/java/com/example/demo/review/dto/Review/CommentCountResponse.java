package com.example.demo.review.dto.Review;

import lombok.Builder;
import lombok.Data;

//댓글 수 조회 Response용 DTO
@Data
@Builder
public class CommentCountResponse {

    private Long reviewId;
    private long commentCount;
}
