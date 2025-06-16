package com.example.demo.property.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.property.util.PropertyDtoConverter.convertImages;
import static com.example.demo.property.util.PropertyDtoConverter.convertSummary;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" },
        justification = "DTO 클래스이며, 외부로부터 받은 리스트는 변경하지 않고 읽기 전용으로 사용됩니다."
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBasicInfoResponseDto {

    private Long propertyId; //매물아이디(기본키)
    private String tradeTypeName; //거래유형명
    private String articleName; //매물이름
    private BigDecimal warrantPrice; //보증금 또는 전세
    private BigDecimal dealPrice; //메매가
    private BigDecimal rentPrice; //월세
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private String articleFeatureDesc; //매물특징요약
    private String realestateTypeName; //부동산유형명
    private String area2; //전용면적
    private String correspondingFloorCount; //해당층수
    private String parkingPossibleYN; //주차가능여부
    private Boolean isBookmarked; //찜여부
    private String exposeStartYMD; //노출시작일
    private List<String> summary; //요약 키워드
    private List<ImageDto> images; //이미지 리스트






     //빌더
    public static PropertyBasicInfoResponseDto of(Property property, PropertySummary summary, List<Image> images,Boolean isBookmarked){
        return PropertyBasicInfoResponseDto.builder()
                .propertyId(property.getPropertyId())
                .tradeTypeName(property.getTradeTypeName())
                .articleName(property.getArticleName())
                .dealPrice(property.getDealPrice())
                .warrantPrice(property.getWarrantPrice())
                .rentPrice(property.getRentPrice())
                .dealOrWarrantPrc(property.getDealOrWarrantPrc())
                .articleFeatureDesc(property.getArticleFeatureDesc())
                .realestateTypeName(property.getRealEstateTypeName())
                .area2(property.getArea2())
                .correspondingFloorCount(property.getCorrespondingFloorCount())
                .parkingPossibleYN(property.getParkingPossibleYN())
                .isBookmarked(isBookmarked)
                .exposeStartYMD(property.getExposeStartYMD())
                .summary(List.copyOf(convertSummary(summary)))
                .images(List.copyOf(convertImages(images)))
                .build();
    }

    //오버로딩 메서드
    public static PropertyBasicInfoResponseDto of (Property property , PropertySummary summary, List<Image> images ){
     return  of(property,summary,images , false);
    }

}
