package com.example.demo.Filter.dto.request;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.Region;
import com.example.demo.Filter.domain.enums.RealEstateTypeName;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import com.example.demo.notification.domain.Notification;
import com.example.demo.notification.dto.NotificationResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterRequestDto {

    private String x;                 // 경도

    private String y;                // 위도

    private String bCode;           // 법정 코드

    private String hCode;           // 행정 코드

    private TradeTypeName tradeTypeName;   // 거래 타입

    private RealEstateTypeName realEstateTypeName;     // 매물타입

    private String placeName;                             // 사용자가 검색한 데이터와 유사한 값

    private String dealOrWarrantPrc;                   // 보증금/전세금/매매가 중에 하나가 들어옴

    private BigDecimal rentPrice;                      //월세

    private LocalDateTime createdAt;                   // 필터 생성시각

    // dto -> entity
    public Filter toEntity(Region region) {
        return Filter.builder()
                .region(region)
                .filterTitle(buildFilterTitle())
                .x(this.x)
                .y(this.y)
                .bCode(this.bCode)
                .hCode(this.hCode)
                .tradeTypeName(this.tradeTypeName)
                .realEstateTypeName(this.realEstateTypeName)
                .dealOrWarrantPrc(this.dealOrWarrantPrc)
                .rentPrice(this.rentPrice)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 요청받은 데이터들을 조합하여 filterTitle 생성
    private String buildFilterTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(placeName);
        sb.append("/").append(tradeTypeName);
        sb.append("/").append(realEstateTypeName);

        if (rentPrice.compareTo(BigDecimal.ZERO) == 0) {
            sb.append("/").append(rentPrice);
        }else{
            sb.append("/").append(dealOrWarrantPrc);
        }

        return sb.toString();
    }
}
