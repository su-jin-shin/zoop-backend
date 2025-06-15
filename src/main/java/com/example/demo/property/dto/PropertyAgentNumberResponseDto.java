package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAgentNumberResponseDto {

    private Long propertyId; //중개사 이름

    private String representativeTelNo; //대표전화번호
    private String cellPhoneNo; //휴대전화번호

    //빌더
    public static PropertyAgentNumberResponseDto of(Property property){
        Realty realty = property.getRealty();
        return PropertyAgentNumberResponseDto.builder()
                .propertyId(property.getPropertyId())

                .representativeTelNo(realty.getRepresentativeTelNo())
                .cellPhoneNo(realty.getCellPhoneNo())
                .build();
    }
}
