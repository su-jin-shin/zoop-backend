package com.example.demo.chat.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FilterDto {

    private String regionName;
    private String regionCode;
    private String tradeTypeName;
    private String tradeTypeCode;
    private String realEstateTypeName;
    private String realEstateTypeCode;
    private int dealOrWarrantPrc;
    private int rentPrice;

}