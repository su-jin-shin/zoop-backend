package com.example.demo.review.dto.ReviewComment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class ReviewCommentUpdateRequest {

    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
}
