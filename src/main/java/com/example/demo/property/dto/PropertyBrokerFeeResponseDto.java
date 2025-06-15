package com.example.demo.property.dto;


import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.Realty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBrokerFeeResponseDto {

    private Long propertyId; //매물 아이디 (기본키)
    private BigDecimal maxBrokerFee; //최대 중개보수
    private BigDecimal brokerFee; //중개보수
    private BigDecimal acquisitionTax; //취득세
    private BigDecimal specialTax; //지방교육세

    //빌더
    public static PropertyBrokerFeeResponseDto of(Property property){
        Realty realty = property.getRealty();
        return PropertyBrokerFeeResponseDto.builder()
                .propertyId(property.getPropertyId())
                .maxBrokerFee(realty.getMaxBrokerFee())
                .brokerFee(realty.getBrokerFee())
                .acquisitionTax(property.getAcquisitionTax())
                .specialTax(property.getSpecialTax())
                .build();
    }



}
