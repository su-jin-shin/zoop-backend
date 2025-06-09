package com.example.demo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

public class MyReviewResponse {
    private Long reviewId;
    private String content;
    private String createdAt;
    private int likeCount;
    private int commentCount;
    private ReviewItem item;

    @Getter
    @Setter
    public static class ReviewItem {
        private Long propertyId;
        private String articleName;
    }
}

