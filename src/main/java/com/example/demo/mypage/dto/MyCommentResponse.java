package com.example.demo.mypage.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MyCommentResponse {

    private final Long commentId;     // 댓글 ID (review_comment.comment_id)
    private final String content;     // 내가 쓴 댓글 내용
    private final LocalDate createdAt;
    private final Integer likeCount;
    private final Review review;
    private final boolean isLiked;

    public MyCommentResponse(Long commentId, String content, LocalDate createdAt, Integer likeCount, Review review, boolean isLiked) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.review = review;
        this.isLiked = isLiked;
    }

    @Getter
    public static class Review {
        private final Long reviewId;   // 댓글이 달린 리뷰 ID
        private final String content;  // 댓글이 달린 리뷰 내용
        private final Item item;

        public Review(Long reviewId, String content, Item item) {
            this.reviewId = reviewId;
            this.content = content;
            this.item = item;
        }
    }

    @Getter
    public static class Item {
        private final Long complexId;     // 존재하면 단지 기반
        private final Long propertyId;    // 존재하면 매물 기반
        private final String articleName; // 단지명 or 매물명

        public Item(Long complexId, Long propertyId, String articleName) {
            this.complexId = complexId;
            this.propertyId = propertyId;
            this.articleName = articleName;
        }
    }
}
