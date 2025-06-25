package com.example.demo.review.dto.ReviewComment.Response;

import lombok.*;
// 댓글 수 세기 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeCountResponse {
    private Long commentId;
    private Long reviewId;
    private Long likeCount;
}

