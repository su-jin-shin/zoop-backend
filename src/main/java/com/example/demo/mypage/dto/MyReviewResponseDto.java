package com.example.demo.mypage.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyReviewResponseDto {
    private Long reviewId;
    private String content;
    private LocalDate createdAt;
    private int likeCount;
    private int commentCount;

    private ItemDto item;

    @Getter
    @Builder
    public static class ItemDto {
        private Long propertyId;
        private String articleName;
    }
}
