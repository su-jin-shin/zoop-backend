package com.example.demo.realty.dto;

import com.example.demo.property.dto.PropertyListItemDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
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

    private List<PropertyListItemDto> property; //매물
    private int page;
    private int size;
    private boolean hasNext;

}
