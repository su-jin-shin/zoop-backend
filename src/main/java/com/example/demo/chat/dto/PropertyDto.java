package com.example.demo.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PropertyDto {

    private int order;
    private Long propertyId;
    private String tradeTypeName;
    private int rentPrice;
    private int warrantPrice;
    private int dealPrice;
    private String dealOrWarrantPrc;
    private List<String> tagList; // summary
    private String articleName;
    private String realEstateTypeName;
    private int netArea; // 전용면적(area2)
    private String imageUrl;
    private Double latitude;
    private Double longitude;

}
