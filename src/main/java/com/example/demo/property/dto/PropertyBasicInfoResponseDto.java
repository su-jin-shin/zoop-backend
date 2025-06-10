package com.example.demo.property.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBasicInfoResponseDto {

    private Long propertyId; //매물아이디(기본키)
    private String tradeTypeName; //거래유형명
    private String articleName; //매물이름
    private BigDecimal warrantPrice; //보증금 또는 전세
    private BigDecimal dealPrice; //메메가
    private BigDecimal rentPrice; //월세
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private String articleFeatureDesc; //매물특징요약
    private String realestateTypeName; //부동산유형명
    private String buildingTypeName; // 매물부동산유형명2
    private String area2; //전용면적
    private String correspondingFloorCount; //해당층수
    private String parkingPossibleYN; //주차가능여부
    private boolean isBookmarked; //찜여부
    private String exposeStartYMD; //노출시작일
    private List<String> summary; //요약 키워드
    private List<ImageDto> images; //이미지 리스트


    //summary 데이터 타입  String -> 리스트 변환 메서드
    public static List<String> convertSummary(PropertySummary summaryEntity){
        if(summaryEntity == null || summaryEntity.getSummary() == null || summaryEntity.getSummary().isBlank()){
            return Collections.emptyList();
        }
        return Arrays.stream(summaryEntity.getSummary().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }


    //images 변환 메서드 (List<Image> -> List<ImageDto> 변환)
    public static List<ImageDto> convertImages(List<Image> images) {
        if(images == null || images.isEmpty()){
            return Collections.emptyList();
        }
        return images.stream()
                .map(ImageDto::from)
                .collect(Collectors.toList());
    }


     //빌더
    public static PropertyBasicInfoResponseDto of(Property p, PropertySummary summary, List<Image> images){
        return PropertyBasicInfoResponseDto.builder()
                .propertyId(p.getPropertyId())
                .tradeTypeName(p.getTradeTypeName())
                .articleName(p.getArticleName())
                .dealPrice(p.getDealPrice())
                .warrantPrice(p.getWarrantPrice())
                .rentPrice(p.getRentPrice())
                .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                .articleFeatureDesc(p.getArticleFeatureDesc())
               // .realestateTypeName(p.getRealestateTypeName())
               // .buildingTypeName(p.getBuildingTypeName())
                .area2(p.getArea2())
                .correspondingFloorCount(p.getCorrespondingFloorCount())
                .parkingPossibleYN(p.getParkingPossibleYN())
                .isBookmarked(false) //찜 기능 생성시 추후 다시 구현
                .exposeStartYMD(p.getExposeStartYMD())
                .summary(convertSummary(summary))
                .images(convertImages(images))
                .build();
    }

}
