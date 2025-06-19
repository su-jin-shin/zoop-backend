package com.example.demo.review.dto.ReviewComment;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeCountResponse {
    private Long commentId;
    private Long reviewId;
    private Long likeCount;
}

