package com.example.demo.review.dto.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.math.BigDecimal;


@Getter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewUpdateRequest {

    @NotNull(message = "별점은 필수입니다.") //미선택 방지 (0점 불가)
    @DecimalMin(value = "0.5", message ="별점은 최소 0.5 이상이어야 합니다.") //0을 선택시 호출되는 메시지
    private BigDecimal rating;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
}
