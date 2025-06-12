package com.example.demo.review.dto.Review;

import lombok.Builder;
import lombok.Data;

import java.util.List;

//리뷰 전체 리스트 응답 DTO
@Data
@Builder
public class ReviewListResponse {
    private Long propertyId;
    private Long complexId;
    private List<ReviewResponse> reviews;

    private long page;   //확장성 고려
    private long size;   //확장성 고려
    private long totalCount;    //확장성 고려
}

