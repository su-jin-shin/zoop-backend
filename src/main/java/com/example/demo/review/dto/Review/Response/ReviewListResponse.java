package com.example.demo.review.dto.Review.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

//리뷰 전체 리스트 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewListResponse {
    private Long propertyId;
    private Long complexId;
    private BigDecimal avgRating; //평균 별점
    private List<ReviewCreateResponse> reviews;
    private Long page;
    private Long size;
    private Long totalCount;
}

