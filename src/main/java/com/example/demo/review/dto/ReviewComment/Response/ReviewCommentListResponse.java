package com.example.demo.review.dto.ReviewComment.Response;



import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.util.List;
// 한 리뷰에 대한 댓글 목록 조회 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentListResponse {
    private Long reviewId;
    private List<ReviewCommentCreateResponse> comments;
    private Long commentCount;
    private Long page;
    private Long size;
    private Long totalCount;
}

