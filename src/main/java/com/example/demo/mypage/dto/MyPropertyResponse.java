package com.example.demo.mypage.dto;

import com.example.demo.property.dto.ImageDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPropertyResponse {

    private Integer order;
    private Long propertyId;
    private String tradeTypeName;
    private BigDecimal rentPrice;
    private BigDecimal warrantPrice;
    private BigDecimal dealPrice;
    private String dealOrWarrantPrc;
    private List<String> summary;
    private String aptName;
    private String buildingName;
    private String realEstateTypeName;
    private String area2;
    private Double latitude;
    private Double longitude;

    private Boolean isBookmarked;

    private String articleName;
    private ImageDto thumbnail;



}
