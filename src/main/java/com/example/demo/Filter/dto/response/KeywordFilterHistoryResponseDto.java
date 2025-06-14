package com.example.demo.Filter.dto.response;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.enums.RealEstateTypeName;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeywordFilterHistoryResponseDto {

    private String x;                 // 경도

    private String y;                // 위도

    private String bCode;           // 법정 코드

    private String hCode;           // 행정 코드

    private TradeTypeName tradeTypeName;   // 거래 타입

    private RealEstateTypeName realEstateTypeName;     // 매물타입

    private String placeName;                             // 사용자가 검색한 데이터와 유사한 값

    private String dealOrWarrantPrc;                   // 보증금/전세금/매매가 중에 하나가 들어옴

    private BigDecimal rentPrice;                      //월세


    // entity -> dto
    public static KeywordFilterHistoryResponseDto from(Filter filter){
        return KeywordFilterHistoryResponseDto.builder()
                .placeName(filter.getFilterTitle())
                .x(filter.getX())
                .y(filter.getY())
                .bCode(filter.getBCode())
                .hCode(filter.getHCode())
                .tradeTypeName(filter.getTradeTypeName())
                .realEstateTypeName(filter.getRealEstateTypeName())
                .rentPrice(filter.getRentPrice())
                .build();
    }
}
