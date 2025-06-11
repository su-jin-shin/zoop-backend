package com.example.demo.mypage.dto;

import java.time.LocalDate;

public interface MyCommentQueryDto {
    Long getCommentId();
    String getContent();
    LocalDate getCreatedAt();
    Integer getLikeCount();

    Long getReviewId();
    String getReviewContent();

    Long getComplexId();
    Long getPropertyId();
    String getArticleName();
}