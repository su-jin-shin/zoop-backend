package com.example.demo.mypage.dto;

import com.example.demo.realty.dto.PropertyListItemDto;
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

    public PropertyListItemDto toPropertyListItemDto() {
        return PropertyListItemDto.builder()
                .propertyId(this.propertyId)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
