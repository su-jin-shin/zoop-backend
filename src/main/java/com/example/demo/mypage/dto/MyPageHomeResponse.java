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

    private MyPageHomeResponse(MyPageUserDto userInfo,
                               List<?> reviewOrComments,
                               List<PropertyListItemDto> bookmarkedProperties,
                               List<PropertyListItemDto> recentViewedProperties) {
        this.userInfo = userInfo == null ? null : new MyPageUserDto(userInfo);
        this.reviewOrComments = reviewOrComments == null ? null : new ArrayList<>(reviewOrComments);
        this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
        this.recentViewedProperties = recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MyPageUserDto userInfo;
        private List<?> reviewOrComments;
        private List<PropertyListItemDto> bookmarkedProperties;
        private List<PropertyListItemDto> recentViewedProperties;

        public Builder userInfo(MyPageUserDto userInfo) {
            this.userInfo = userInfo == null ? null : new MyPageUserDto(userInfo);
            return this;
        }

        public Builder reviewOrComments(List<?> reviewOrComments) {
            this.reviewOrComments = reviewOrComments == null ? null : new ArrayList<>(reviewOrComments);
            return this;
        }

        public Builder bookmarkedProperties(List<PropertyListItemDto> bookmarkedProperties) {
            this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
            return this;
        }

        public Builder recentViewedProperties(List<PropertyListItemDto> recentViewedProperties) {
            this.recentViewedProperties = recentViewedProperties == null ? null : new ArrayList<>(recentViewedProperties);
            return this;
        }

        public MyPageHomeResponse build() {
            return new MyPageHomeResponse(userInfo, reviewOrComments, bookmarkedProperties, recentViewedProperties);
        }
    }

}
