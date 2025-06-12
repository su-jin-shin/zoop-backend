package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTransactionResponseDto {

    private Long propertyId; //매물 아이디 (기본키)
    private String tradeTypeName; //거래유형명
    private BigDecimal warrantPrice; //보증금
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private BigDecimal etcFeeAmount; // 관리비(기타비용)
    private BigDecimal financePrice; //융자가격
    private String moveInPossibleYmd; //입주가능일
    private BigDecimal dealPrice; //매매가
    private BigDecimal rentPrice; //월세

    //빌더
    public static PropertyTransactionResponseDto of(Property property){
        return PropertyTransactionResponseDto.builder()
                .propertyId(property.getPropertyId())
                .tradeTypeName(property.getTradeTypeName())
                .warrantPrice(property.getWarrantPrice())
                .dealOrWarrantPrc(property.getDealOrWarrantPrc())
                .etcFeeAmount(property.getEtcFeeAmount())
                .financePrice(property.getFinancePrice())
                .moveInPossibleYmd(property.getMoveInPossibleYmd())
                .dealPrice(property.getDealPrice())
                .rentPrice(property.getRentPrice())
                .build();
    }



}
