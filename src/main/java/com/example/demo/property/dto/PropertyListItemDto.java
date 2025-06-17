package com.example.demo.property.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyListItemDto {

    private Integer order; // 정렬 순서
    private Long propertyId; //매물아이디(기본키)
    private String tradeTypeName; //거래유형명
    private BigDecimal rentPrice; //월세
    private BigDecimal warrantPrice; //보증금
    private BigDecimal dealPrice; //매매가
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private List<String> summary; //요약 키워드
    private String aptName; //아파트이름
    private String buildingName; //건물이름
    private String realEstateTypeName;//부동산 유형명
    private String area2; //전용면적
    private Double latitude; //위도
    private Double longitude; //경도

    private Boolean isBookmarked; // 찜 여부


    private String articleName; //매물이름
    private ImageDto thumbnail;//썸네일


}
