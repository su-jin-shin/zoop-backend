package com.example.demo.review.dto.ReviewComment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 리뷰 댓글 단건 Response용 DTO  -> 나중에 Mapper랑 합치던지...
@Data
@Builder
public class ReviewCommentResponse {

    private Long commentId;
    private Long reviewId;

    private Long userId;
    private String nickname;
    private String profileImage;

    private String content;

    private long likeCount;  //nullable
    private boolean isLikedByMe;
    private boolean isMine;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}

