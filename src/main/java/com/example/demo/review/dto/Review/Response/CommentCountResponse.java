package com.example.demo.review.dto.Review.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

//댓글 수 조회 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CommentCountResponse {
    private Long reviewId;
    private Long commentCount;
}
