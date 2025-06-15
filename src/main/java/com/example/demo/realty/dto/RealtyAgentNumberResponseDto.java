package com.example.demo.realty.dto;

import com.example.demo.realty.domain.Realty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtyAgentNumberResponseDto {

    private Long realtyId; //부동산 아이디 기본키
    private String representativeTelNo; //대표 전화번호
    private String cellPhoneNo; //휴대 전화번호

    //빌더
    public static RealtyAgentNumberResponseDto of(Realty realty) {
        return RealtyAgentNumberResponseDto.builder()
                .realtyId(realty.getRealtyId())
                .representativeTelNo(realty.getRepresentativeTelNo())
                .cellPhoneNo(realty.getCellPhoneNo())
                .build();
    }
}
