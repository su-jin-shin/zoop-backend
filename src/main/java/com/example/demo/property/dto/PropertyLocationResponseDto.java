package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyLocationResponseDto {

    private Long propertyId; //매물 아이디(기본키)
    private String exposureAddress; //노출 주소
    private Double latitude; //위도
    private Double longitude; //경도

    //빌더
    public static PropertyLocationResponseDto of(Property property){
        return PropertyLocationResponseDto.builder()
                .propertyId(property.getPropertyId())
                .exposureAddress(property.getExposureAddress())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .build();
    }

}
