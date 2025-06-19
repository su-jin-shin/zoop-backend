package com.example.demo.mypage.dto;

import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.realty.dto.RealtyPropertyListResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MyPageHomeResponse {
    private final MyPageUserDto userInfo;
    private final List<?> reviewOrComments;
    private final List<PropertyListItemDto> bookmarkedProperties;
    private final List<PropertyListItemDto> recentViewedProperties;

    @Builder
    public MyPageHomeResponse(MyPageUserDto userInfo,
                              List<?> reviewOrComments,
                              List<PropertyListItemDto> bookmarkedProperties,
                              List<PropertyListItemDto> recentViewedProperties) {
        this.userInfo = userInfo == null ? null : new MyPageUserDto(userInfo); // 복사 생성자 필요
        this.reviewOrComments = reviewOrComments == null ? null : new ArrayList<>(reviewOrComments);
        this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
        this.recentViewedProperties = recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
    }

    public List<?> getReviewOrComments() {
        return reviewOrComments == null ? null : new ArrayList<>(reviewOrComments);
    }

    public List<PropertyListItemDto> getBookmarkedProperties() {
        return bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
    }

    public List<PropertyListItemDto> getRecentViewedProperties() {
        return recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
    }

    public MyPageUserDto getUserInfo() {
        return userInfo == null ? null : new MyPageUserDto(userInfo);
    }
}