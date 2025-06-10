package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDescriptionResponseDto {

    private Long propertyId; //매물 아이디 (기본키)
    private String articleFeatureDescription; //매물특징설명
    private String detailDescription; //상세설명


    //빌더
    public static PropertyDescriptionResponseDto of(Property p){
        return PropertyDescriptionResponseDto.builder()
                .propertyId(p.getPropertyId())
                .articleFeatureDescription(p.getArticleFeatureDescription())
                .detailDescription(p.getDetailDescription())
                .build();
    }


}
