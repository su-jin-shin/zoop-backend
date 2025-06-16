package com.example.demo.review.dto.ReviewComment;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.time.LocalDateTime;

// 리뷰 댓글 단건 Response용 DTO  -> 나중에 Mapper랑 합치던지...
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentResponse {

    private Long commentId;
    private Long reviewId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;

    private long likeCount;
    private Boolean isLikedByMe;
    private Boolean isMine;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

