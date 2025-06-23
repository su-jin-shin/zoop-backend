package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtyWithPropertiesResponseDto {
    private Long propertyId; //매물 아이디(인조)
    private String realtorName; //중개사 이름
    private String establishRegistrationNo;// 개설등록번호
    private String address;//중개사 주소
    private Integer dealCount; // 매매
    private Integer leaseCount; // 전세
    private Integer rentCount; // 월세
    private String representativeName;//대표자이름
    private String representativeTelNo;//대표전화번호
    private String cellPhoneNo;//휴대전화번호

    public static RealtyWithPropertiesResponseDto of(Property property,
                                                     int dealCount, int leaseCount, int rentCount) {
        Realty realty = property.getRealty();

        return RealtyWithPropertiesResponseDto.builder()
                .propertyId(property.getPropertyId())
                .realtorName(realty.getRealtorName())
                .establishRegistrationNo(realty.getEstablishRegistrationNo())
                .address(realty.getAddress())
                .dealCount(dealCount)
                .leaseCount(leaseCount)
                .rentCount(rentCount)
                .representativeName(realty.getRepresentativeName())
                .representativeTelNo(realty.getRepresentativeTelNo())
                .cellPhoneNo(realty.getCellPhoneNo())
                .build();
    }
}
