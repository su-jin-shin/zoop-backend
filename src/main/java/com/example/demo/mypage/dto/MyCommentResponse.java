package com.example.demo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MyCommentResponse {

    private Long commentId;     // 댓글 ID (review_comment.comment_id)
    private String content;     // 내가 쓴 댓글 내용
    private LocalDate createdAt;
    private Integer likeCount;

    private Review review;

    @Getter
    @Setter
    public static class Review {
        private Long reviewId;   // 댓글이 달린 리뷰 ID
        private String content;  // 댓글이 달린 리뷰 내용
        private Item item;
    }

    @Getter
    @Setter
    public static class Item {
        private Long complexId;     // 존재하면 단지 기반
        private Long propertyId;    // 존재하면 매물 기반
        private String articleName; // 단지명 or 매물명
    }
}
