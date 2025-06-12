package com.example.demo.mypage.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentViewedPropertyResponse {

    private Integer order; // 정렬 순서
    private Long propertyId;
    private String tradeTypeName;
    private BigDecimal rentPrice;
    private BigDecimal warrantPrice;
    private BigDecimal dealPrice;
    private String dealOrWarrantPrc;
    private List<String> summary;
    private String articleName;
    private String aptName;
    private String buildingName;
    private String buildingTypeName;
    private String realestateTypeName;
    private String area1;
    private Boolean isActive;
    private Boolean isBookmarked;
    private String thumbnailImage;
}
