package com.example.demo.common.excel;

import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyExcelDto {

    private Integer order;
    private Long propertyId;
    private String articleName;
    private String tradeTypeName;
    private BigDecimal rentPrice;
    private BigDecimal warrantPrice;
    private BigDecimal dealPrice;
    private BigDecimal etcFeeAmount;
    private String moveInPossibleYmd;
    private String articleFeatureDesc;
    private String detailDescription;
    private String area1;
    private String area2;
    private String direction;
    private String floorInfo;
    private String exposureAddress;
    private Double latitude;
    private Double longitude;
    private List<String> tagList;

    private String realtorName;
    private String representativeName;
    private String establishRegistrationNo;
    private String realtorAddress;
    private String representativeTelNo;
    private String cellPhoneNo;
    private BigDecimal maxBrokerFee;
    private BigDecimal brokerFee;

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
                .area1(property.getArea1())
                .area2(property.getArea2())
                .direction(property.getDirection())
                .floorInfo(property.getFloorInfo())
                .exposureAddress(property.getExposureAddress())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .tagList(property.getTagList())
                .realtorName(realtor.getRealtorName())
                .representativeName(realtor.getRepresentativeName())
                .establishRegistrationNo(realtor.getEstablishRegistrationNo())
                .realtorAddress(realtor.getAddress())
                .representativeTelNo(realtor.getRepresentativeTelNo())
                .cellPhoneNo(realtor.getCellPhoneNo())
                .maxBrokerFee(realtor.getMaxBrokerFee())
                .brokerFee(realtor.getBrokerFee())
                .build();
    }
}
