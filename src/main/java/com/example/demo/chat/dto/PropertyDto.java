package com.example.demo.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private int area2; // 전용면적
    private String imageUrl;
    private Double latitude;
    private Double longitude;

    public List<String> getTagList() {
        return tagList == null ? null : new ArrayList<>(tagList); // 복사본 반환
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList == null ? null : new ArrayList<>(tagList); // 복사본 저장
    }

}
