package com.example.demo.review.dto.ReviewComment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReviewCommentCreateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
