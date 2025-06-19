package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtyWithPropertiesResponseDto {
    private Long  propertyId; //매물 아이디(인조)
    private String realtorName; //중개사이름
    private String establishRegistrationNo; //개설등록번호
    private String address; //중개사주소
    private Integer dealCount; //매매건수
    private Integer leaseCount; //전세건수
    private Integer rentCount; //월세 건수
    private String representativeName; //대표자이름
    private String representativeTelNo; //대표 전화번호
    private String cellPhoneNo; //휴대전화번호

    //빌더
    public static RealtyWithPropertiesResponseDto of(Property property){
        Realty realty = property.getRealty();
        return RealtyWithPropertiesResponseDto.builder()
                .propertyId(property.getPropertyId())
                .realtorName(realty.getRealtorName())
                .establishRegistrationNo(realty.getEstablishRegistrationNo())
                .address(realty.getAddress())
                .dealCount(realty.getDealCount())
                .leaseCount(realty.getLeaseCount())
                .rentCount(realty.getRentCount())
                .representativeName(realty.getRepresentativeName())
                .representativeTelNo(realty.getRepresentativeTelNo())
                .cellPhoneNo(realty.getCellPhoneNo())
                .build();
    }


}
