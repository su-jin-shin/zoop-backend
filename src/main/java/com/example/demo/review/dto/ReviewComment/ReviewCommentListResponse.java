package com.example.demo.review.dto.ReviewComment;

//리뷰 댓글 리스트 조회용 Response DTO

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewCommentListResponse {

    private Long reviewId;
    private Long commentCount;
    private List<ReviewCommentResponse> comments;

    private long page;   //확장성 고려
    private long size;   //확장성 고려
    private long totalCount;    //확장성 고려
}
