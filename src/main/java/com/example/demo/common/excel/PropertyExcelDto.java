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

    public PropertyExcelDto(
            Integer order, Long propertyId, String tradeTypeName,
            BigDecimal rentPrice, BigDecimal warrantPrice, BigDecimal dealPrice,
            String dealOrWarrantPrc, List<String> summary,
            String aptName, String buildingName, String realEstateTypeName,
            String area2, Double latitude, Double longitude, Boolean isBookmarked,
            String articleName, String imageUrl, String direction, String floorInfo, String exposureAddress,
            BigDecimal etcFeeAmount, String moveInPossibleYmd, String articleFeatureDesc, String detailDescription,
            String realtorName, String representativeName, String realtorAddress, String representativeTelNo,
            String cellPhoneNo, BigDecimal maxBrokerFee, BigDecimal brokerFee
    ) {
        this.order = order;
        this.propertyId = propertyId;
        this.tradeTypeName = tradeTypeName;
        this.rentPrice = rentPrice;
        this.warrantPrice = warrantPrice;
        this.dealPrice = dealPrice;
        this.dealOrWarrantPrc = dealOrWarrantPrc;
        this.summary = summary == null ? null : new ArrayList<>(summary); // ✅ 방어적 복사
        this.aptName = aptName;
        this.buildingName = buildingName;
        this.realEstateTypeName = realEstateTypeName;
        this.area2 = area2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isBookmarked = isBookmarked;
        this.articleName = articleName;
        this.imageUrl = imageUrl;
        this.direction = direction;
        this.floorInfo = floorInfo;
        this.exposureAddress = exposureAddress;
        this.etcFeeAmount = etcFeeAmount;
        this.moveInPossibleYmd = moveInPossibleYmd;
        this.articleFeatureDesc = articleFeatureDesc;
        this.detailDescription = detailDescription;
        this.realtorName = realtorName;
        this.representativeName = representativeName;
        this.realtorAddress = realtorAddress;
        this.representativeTelNo = representativeTelNo;
        this.cellPhoneNo = cellPhoneNo;
        this.maxBrokerFee = maxBrokerFee;
        this.brokerFee = brokerFee;
    }

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
                .summary(safeList(property.getTagList())) // ✅ 복사 안전하게 처리

                .realtorName(realtor != null ? realtor.getRealtorName() : null)
                .representativeName(realtor != null ? realtor.getRepresentativeName() : null)
                .realtorAddress(realtor != null ? realtor.getAddress() : null)
                .representativeTelNo(realtor != null ? realtor.getRepresentativeTelNo() : null)
                .cellPhoneNo(realtor != null ? realtor.getCellPhoneNo() : null)
                .maxBrokerFee(realtor != null ? realtor.getMaxBrokerFee() : null)
                .brokerFee(realtor != null ? realtor.getBrokerFee() : null)

                .build();
    }

    // PropertyListItemDto 기반 생성 메서드
    public static PropertyExcelDto from(com.example.demo.realty.dto.PropertyListItemDto dto) {
        return PropertyExcelDto.builder()
                .order(dto.getOrder())
                .propertyId(dto.getPropertyId())
                .tradeTypeName(dto.getTradeTypeName())
                .rentPrice(dto.getRentPrice())
                .warrantPrice(dto.getWarrantPrice())
                .dealPrice(dto.getDealPrice())
                .dealOrWarrantPrc(dto.getDealOrWarrantPrc())
                .summary(safeList(dto.getSummary()))
                .aptName(dto.getAptName())
                .buildingName(dto.getBuildingName())
                .realEstateTypeName(dto.getRealEstateTypeName())
                .area2(dto.getArea2())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isBookmarked(dto.getIsBookmarked())
                .articleName(dto.getArticleName())
                .imageUrl(dto.getImageUrl())
                .build();
    }


    private static List<String> safeList(List<String> list) {
        return list == null ? Collections.emptyList() : new ArrayList<>(list);
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "방어적 복사 적용됨")
    public List<String> getSummary() {
        return summary == null ? Collections.emptyList() : new ArrayList<>(summary);
    }

    public void setSummary(List<String> summary) {
        this.summary = summary == null ? null : new ArrayList<>(summary);
    }

    public static class PropertyExcelDtoBuilder {
        public PropertyExcelDtoBuilder summary(List<String> summary) {
            this.summary = summary == null ? null : new ArrayList<>(summary);
            return this;
        }
    }

    public String getDealOrWarrantPrc() {
        if (dealPrice != null) return dealPrice.toPlainString();
        if (warrantPrice != null) return warrantPrice.toPlainString();
        return "-";
    }

}