package com.example.demo.review.dto.Review.Request;



import com.example.demo.review.domain.enums.HasChildren;
import com.example.demo.review.domain.enums.IsResident;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
// 리뷰 작성 요청
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCreateRequest {

    @NotNull(message = "별점은 필수입니다.")
    @DecimalMin(value = "0.5", message = "별점은 최소 0.5 이상이어야 합니다.")
    private BigDecimal rating;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "자녀 여부는 필수입니다.")
    private HasChildren hasChildren;

    @NotNull(message = "거주 여부는 필수입니다.")
    private IsResident isResident;
}

