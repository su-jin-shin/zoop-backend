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
@AllArgsConstructor
public class MyPageHomeResponse {
    private MyPageUserDto userInfo;
    private List<?> reviewOrComments;
    private List<PropertyListItemDto> bookmarkedProperties;
    private List<PropertyListItemDto> recentViewedProperties;

    @Builder
    public static MyPageHomeResponse create(MyPageUserDto userInfo,
                                            List<?> reviewOrComments,
                                            List<PropertyListItemDto> bookmarkedProperties,
                                            List<PropertyListItemDto> recentViewedProperties) {
        return new MyPageHomeResponse(
                userInfo,
                reviewOrComments == null ? null : new ArrayList<>(reviewOrComments),
                bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties),
                recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties)
        );
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
}
