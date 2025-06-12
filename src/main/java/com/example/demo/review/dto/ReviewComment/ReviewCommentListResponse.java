package com.example.demo.review.dto.ReviewComment;

//리뷰 댓글 리스트 조회용 Response DTO

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentListResponse {

    private Long reviewId;
    private Long commentCount;
    private List<ReviewCommentResponse> comments;

    private long page;   //확장성 고려
    private long size;   //확장성 고려
    private long totalCount;    //확장성 고려
}
