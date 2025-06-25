package com.example.demo.review.dto.ReviewComment.Request;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
// 댓글 작성 요청
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentCreateRequest {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}

