package com.example.demo.common.excel;

import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyExcelDto {

    private Integer order; // 정렬 순서
    private Long propertyId; // 매물 아이디
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
    private String imageUrl; //이미지 경로

    private String direction; //방향
    private String floorInfo; //층정보
    private String exposureAddress; //노출주소

    private BigDecimal etcFeeAmount; //관리비(기타비용)
    private String moveInPossibleYmd; //입주가능일
    private String articleFeatureDesc; //매물특징요약
    private String detailDescription; //상세설명

    private String realtorName; //중개사이름
    private String representativeName; //대표 이름
    private String realtorAddress; //중개사주소
    private String representativeTelNo; //대표전화번호
    private String cellPhoneNo; //휴대전화번호
    private BigDecimal maxBrokerFee; //최대 중개보수
    private BigDecimal brokerFee; //중개보수

    public static PropertyExcelDto from(Property property, Realty realtor, int order) {
        return PropertyExcelDto.builder()
                .order(order)
                .propertyId(property.getPropertyId())
                .articleName(property.getArticleName())
                .tradeTypeName(property.getTradeTypeName())
                .rentPrice(property.getRentPrice())
                .warrantPrice(property.getWarrantPrice())
                .dealPrice(property.getDealPrice())
                .etcFeeAmount(property.getEtcFeeAmount())
                .moveInPossibleYmd(property.getMoveInPossibleYmd())
                .articleFeatureDesc(property.getArticleFeatureDesc())
                .detailDescription(property.getDetailDescription())
                .area2(property.getArea2())
                .direction(property.getDirection())
                .floorInfo(property.getFloorInfo())
                .exposureAddress(property.getExposureAddress())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .summary(safeList(property.getTagList())).realtorName(realtor.getRealtorName())
                .representativeName(realtor.getRepresentativeName())
                .realtorAddress(realtor.getAddress())
                .representativeTelNo(realtor.getRepresentativeTelNo())
                .cellPhoneNo(realtor.getCellPhoneNo())
                .maxBrokerFee(realtor.getMaxBrokerFee())
                .brokerFee(realtor.getBrokerFee())
                .build();
    }

    private static List<String> safeList(List<String> list){
        return  list == null ? Collections.emptyList() : list;
    }

    public List<String> getSummary() {
        return summary == null ? Collections.emptyList() : new ArrayList<>(summary);
    }
}