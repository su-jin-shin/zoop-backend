package com.example.demo.chat.dto;

import com.example.demo.Filter.domain.enums.RealEstateTypeName;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class FilterDto {

    private String x; // 경도
    private String y; // 위도
    private String placeName; // 사용자가 검색한 데이터와 유사한 값 //private String regionName;
    @JsonProperty("bCode")
    private String bCode; // 법정 코드 //private String regionCode;
    @JsonProperty("hCode")
    private String hCode; // 행정 코드
    private String tradeTypeName;
    private String tradeTypeCode;
    private String realEstateTypeName;
    private String realEstateTypeCode;
    private int dealOrWarrantPrc;
    private int rentPrice;

}