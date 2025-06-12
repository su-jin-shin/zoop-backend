package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.Realty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAgentResponseDto {

    private Long propertyId; //매물 아이디 (기본키)

    private String realtorName; //중개사이름
    private String representativeName; //대표자이름
    private String establishRegistrationNo; //개설등록번호
    private String address; //중개사주소
    private String representativeTelNo; //대표전화번호
    private String cellPhoneNo; //휴대전화번호

    //빌더
    public static PropertyAgentResponseDto of(Property property){
        Realty realty =property.getRealty();
        return PropertyAgentResponseDto.builder()
                .propertyId(property.getPropertyId())

                .realtorName(realty.getRealtorName())
                .representativeName(realty.getRepresentativeName())
                .establishRegistrationNo(realty.getEstablishRegistrationNo())
                .address(realty.getAddress())
                .representativeTelNo(realty.getRepresentativeTelNo())
                .cellPhoneNo(realty.getCellPhoneNo())
                .build();
    }


}
