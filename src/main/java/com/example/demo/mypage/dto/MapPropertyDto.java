package com.example.demo.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MapPropertyDto {
    private Long propertyId;
    private Double latitude;
    private Double longitude;
}
