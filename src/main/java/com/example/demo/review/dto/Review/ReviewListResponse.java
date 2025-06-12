package com.example.demo.review.dto.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.util.List;

//리뷰 전체 리스트 응답 DTO
@Data
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewListResponse {
    private Long propertyId;
    private Long complexId;
    private List<ReviewResponse> reviews;

    private long page;   //확장성 고려
    private long size;   //확장성 고려
    private long totalCount;    //확장성 고려
}

