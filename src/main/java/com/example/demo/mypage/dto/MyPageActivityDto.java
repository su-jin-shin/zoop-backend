package com.example.demo.mypage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageActivityDto {
    private final int bookmarkedPropertyCount;
    private final int recentViewedPropertyCount;
}
