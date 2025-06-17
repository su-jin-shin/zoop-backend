package com.example.demo.property.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.demo.property.util.PropertyDtoConverter.convertImages;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyOgResponseDto {
    private Long propertyId; //매물 아이디(기본키)
    private String articleName;  //매물이름
    private String articleFeatureDesc; //매물특징요약
    private List<ImageDto> images; //이미지 리스트

    //빌더
    public static PropertyOgResponseDto of(Property property , List<Image> images){
        return PropertyOgResponseDto.builder()
                .propertyId(property.getPropertyId())
                .articleName(property.getArticleName())
                .articleFeatureDesc(property.getArticleFeatureDesc())
                .images(List.copyOf(convertImages(images)))
                .build();
    }
}
