package com.example.demo.mypage.dto;

import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.realty.dto.RealtyPropertyListResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageHomeResponse {
    private MyPageUserDto userInfo;
    private List<?> reviewOrComments;
    private List<PropertyListItemDto> bookmarkedProperties;
    private List<PropertyListItemDto> recentViewedProperties;
}
