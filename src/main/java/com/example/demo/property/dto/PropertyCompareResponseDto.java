package com.example.demo.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCompareResponseDto {
    private Long propertyId; //매물아이디(기본키)
    private String imageUrl; //이미지경로
    private String tradeTypeName; //거래유형명
    private BigDecimal warrantPrice; //보증금
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private BigDecimal dealPrice; //매매가
    private BigDecimal rentPrice; //월세
    private String articleName; //매물이름
    private String useApproveYmd; //사용승인일
    private List<String> tagList; //태그
    private String area1; //공금변적
    private String area2; //전용면적
    private String roomCount; //방개수
    private String bathroomCount; //욕실개수
    private String floorInfo; //층정보
    private String directionBaseTypeName; //기준방향명
    private String direction; //방향
    private BigDecimal etcFeeAmount; //관리비(기타비용)
    private String moveInPossibleYmd; //입주가능일
    private String parkingPossibleYN; //주차가능여부
    private List<String> securityFacilities; //보안시설



}
