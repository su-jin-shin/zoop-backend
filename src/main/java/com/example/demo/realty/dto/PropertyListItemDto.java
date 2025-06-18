package com.example.demo.realty.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.dto.ImageDto;
import com.example.demo.property.dto.ImageUrlOnlyDto;
import com.example.demo.property.util.PropertyDtoConverter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static com.example.demo.property.util.PropertyDtoConverter.convertImages;
import static com.example.demo.property.util.PropertyDtoConverter.convertSummary;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyListItemDto {

    private Integer order; // 정렬 순서
    private Long propertyId; //매물아이디(기본키)
    private String tradeTypeName; //거래유형명
    private BigDecimal rentPrice; //월세
    private BigDecimal warrantPrice; //보증금
    private BigDecimal dealPrice; //매매가
    private String dealOrWarrantPrc; //거래 또는 보증금 가격
    private List<String> summary; //요약 키워드
    private String aptName; //아파트이름
    private String buildingName; //건물이름
    private String realEstateTypeName;//부동산 유형명
    private String area2; //전용면적
    private Double latitude; //위도
    private Double longitude; //경도

    private Boolean isBookmarked; // 찜 여부


    private String articleName; //매물이름
    private String imageUrl; //이미지 경로
    private ImageDto thumbnail;


    //빌더
    public static PropertyListItemDto of(
            Property property,
            PropertySummary summary,
            List<Image> images,
            Boolean isBookmarked,
            int order
    ) {

        String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl(); //첫 이미지 url 사용
        return PropertyListItemDto.builder()
                .order(order)
                .propertyId(property.getPropertyId())
                .tradeTypeName(property.getTradeTypeName())
                .rentPrice(property.getRentPrice())
                .warrantPrice(property.getWarrantPrice())
                .dealPrice(property.getDealPrice())
                .dealOrWarrantPrc(property.getDealOrWarrantPrc())
                .summary(List.copyOf(convertSummary(summary)))
                .aptName(property.getAptName())
                .buildingName(property.getBuildingName())
                .realEstateTypeName(property.getRealEstateTypeName())
                .area2(property.getArea2())
                .latitude(property.getLatitude())
                .isBookmarked(isBookmarked)
                .longitude(property.getLongitude())
                .articleName(property.getArticleName())
                .imageUrl(imageUrl)
                .build();
    }



}
