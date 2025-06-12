package com.example.demo.review.dto.ReviewComment;

import lombok.Builder;
import lombok.Data;

//리뷰 댓글 좋아요 등록/취소용 Response

@Data
@Builder
public class ReviewCommentLikeResponse {

    private Long reviewId;
    private Long commentId;
    private Long userId;
    private boolean isLiked;
}
