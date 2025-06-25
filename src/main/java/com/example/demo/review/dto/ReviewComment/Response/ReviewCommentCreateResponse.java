package com.example.demo.review.dto.ReviewComment.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.time.LocalDateTime;

// 댓글 작성 응답
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentCreateResponse {

    private Long commentId;
    private Long reviewId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;

    private Long likeCount;
    private Boolean isLikedByMe;
    private Boolean isMine;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

