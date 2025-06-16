package com.example.demo.review.dto.ReviewComment;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

//리뷰 댓글 좋아요 등록/취소용 Response

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentLikeResponse {
    private Long reviewId;
    private Long commentId;
    private Long userId;
    private Boolean isLiked;
}
