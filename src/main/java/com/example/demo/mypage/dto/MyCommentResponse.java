package com.example.demo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

public class MyCommentResponse {
    private Long commentId;
    private String content;
    private String createdAt;
    private int likeCount;
    private ReviewInfo review;

    @Getter
    @Setter
    public static class ReviewInfo {
        private Long reviewId;
        private String content;
        private ReviewItem item;

        @Getter
        @Setter
        public static class ReviewItem {
            private Long propertyId;
            private String articleName;
        }
    }
}
