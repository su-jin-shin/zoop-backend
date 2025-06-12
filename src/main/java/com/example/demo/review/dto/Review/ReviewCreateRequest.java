package com.example.demo.review.dto.Review;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class ReviewCreateRequest {
    
    @NotNull(message = "별점은 필수입니다.") //미선택 방지 (0점 불가)
    @DecimalMin(value = "0.5", message ="별점은 최소 0.5 이상이어야 합니다.") //0을 선택시 호출되는 메시지
    private BigDecimal rating;


    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;

    private boolean hasChildren;
    private boolean isResident;

}
