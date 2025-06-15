package com.example.demo.realty.dto;

import com.example.demo.property.dto.PropertyListItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtyWithPropertiesResponseDto {

    private Long  realtyId; //부동산 아이디(인조)
    private String realtorName; //중개사이름
    private String establishRegistrationNo; //개설등록번호
    private String address; //중개사주소
    private Integer dealCount; //매매건수
    private Integer leaseCount; //전세건수
    private Integer rentCount; //월세 건수
    private String representativeName; //대표자이름

    private List<PropertyListItemDto> propertie; //매물

}
