package com.example.demo.review.dto.ReviewComment.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
// 댓글 좋아요 여부 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CommentLikeStatusResponse {
    private Long reviewId;
    private Long commentId;
    private Boolean isLiked;
}

