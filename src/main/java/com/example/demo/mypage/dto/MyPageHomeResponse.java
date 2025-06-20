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
    private int bookmarkedCount;

    private final List<PropertyListItemDto> recentViewedProperties;
    private int recentViewedCount;

    @Builder
    public MyPageHomeResponse(MyPageUserDto userInfo,
                              List<?> reviewOrComments,
                              List<PropertyListItemDto> bookmarkedProperties,
                              int bookmarkedCount,
                              List<PropertyListItemDto> recentViewedProperties,
                              int recentViewedCount) {
        this.userInfo = userInfo == null ? null : new MyPageUserDto(userInfo); // ✅ 방어적 복사
        this.reviewOrComments = reviewOrComments == null ? null : new ArrayList<>(reviewOrComments); // ✅ 방어적 복사
        this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
        this.bookmarkedCount = bookmarkedCount;
        this.recentViewedProperties = recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
        this.recentViewedCount = recentViewedCount;
    }

    // ✅ getter도 복사본 반환
    public MyPageUserDto getUserInfo() {
        return userInfo == null ? null : new MyPageUserDto(userInfo);
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

    // ✅ Builder 내부도 방어적 복사 적용 필요 (아래 추가)
    public static class MyPageHomeResponseBuilder {
        public MyPageHomeResponseBuilder userInfo(MyPageUserDto userInfo) {
            this.userInfo = userInfo == null ? null : new MyPageUserDto(userInfo);
            return this;
        }

        public MyPageHomeResponseBuilder reviewOrComments(List<?> reviewOrComments) {
            this.reviewOrComments = reviewOrComments == null ? null : new ArrayList<>(reviewOrComments);
            return this;
        }

        public MyPageHomeResponseBuilder bookmarkedProperties(List<PropertyListItemDto> bookmarkedProperties) {
            this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
            return this;
        }

        public MyPageHomeResponseBuilder recentViewedProperties(List<PropertyListItemDto> recentViewedProperties) {
            this.recentViewedProperties = recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
            return this;
        }
    }
}
