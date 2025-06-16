package com.example.demo.review.dto.ReviewComment;

//리뷰 댓글 리스트 조회용 Response DTO

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentListResponse {
    private Long reviewId;
    private List<ReviewCommentResponse> comments;
    private long commentCount;
    private long page;
    private long size;
    private long totalCount;
}

