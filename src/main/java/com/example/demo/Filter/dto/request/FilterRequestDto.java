package com.example.demo.Filter.dto.request;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.Region;
import com.example.demo.Filter.domain.enums.TradeTypeCode;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import com.example.demo.common.exception.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class FilterRequestDto {

    private String x;                 // 경도

    private String y;                // 위도

    @JsonProperty("bCode")
    private String bCode;           // 법정 코드

    @JsonProperty("hCode")
    private String hCode;                   // 행정 코드

    private TradeTypeName tradeTypeName;   // 거래 타입    //매매, 전세, 월세

    private List<String> realEstateTypeName;     // 매물타입 // 아파트, 오피스텔, 빌라, 원룸_투룸

    private String placeName;                             // 사용자가 검색한 데이터와 유사한 값

    private String dealOrWarrantPrc;                   // 보증금/전세금/매매가 중에 하나가 들어옴

    private BigDecimal rentPrice;                      //월세

    private static final Map<String, String> ESTATE_TYPE_CODE_MAP = Map.of(
            "아파트", "APT",
            "오피스텔", "OPST",
            "빌라", "VL:YR",
            "원룸_투룸", "DDDGG:DSD"
    );


    // dto -> entity
    public Filter toEntity(Region region) {

        // 매물 타입 이름 -> 코드 리스트 자동 매핑
        List<String> realEstateTypeCodes = this.realEstateTypeName.stream()
                .map(name -> Optional.ofNullable(ESTATE_TYPE_CODE_MAP.get(name))
                        .orElseThrow(() -> new InvalidRequestException())).toList();

        return Filter.builder()
                .region(region)
                .filterTitle(buildFilterTitle())
                .longitude(this.x)
                .latitude(this.y)
                .bCode(this.bCode)
                .hCode(this.hCode)
                .tradeTypeName(this.tradeTypeName)
                .tradeTypeCode(TradeTypeCode.fromName(this.tradeTypeName))
                .realEstateTypeName(this.realEstateTypeName)
                .realEstateTypeCode(realEstateTypeCodes)
                .dealOrWarrantPrc(this.dealOrWarrantPrc)
                .rentPrice(this.rentPrice)
                .createdAt(LocalDateTime.now())
                .build();
    }


    // 요청받은 데이터들을 조합하여 filterTitle 생성
    public String buildFilterTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(placeName);
        sb.append("/").append(tradeTypeName);

        String realEstateTypeNames = String.join(",", realEstateTypeName);
        sb.append("/").append(realEstateTypeNames);

        if (tradeTypeName == TradeTypeName.월세) {
            BigDecimal rent = rentPrice != null ? rentPrice : BigDecimal.ZERO;
            BigDecimal deposit = new BigDecimal(dealOrWarrantPrc);

            sb.append("/").append(formatPrice(rent))
                    .append("(보증금: ").append(formatPrice(deposit)).append(")");
        } else {
            BigDecimal price = new BigDecimal(dealOrWarrantPrc);
            sb.append("/").append(formatPrice(price));
        }

        return sb.toString();
    }

    private String formatPrice(BigDecimal amount) {
        long price = amount.longValue();

        if (price >= 10000) {
            long 억 = price / 10000;
            long 만원 = price % 10000;

            if (만원 == 0) {
                return 억 + "억";
            } else {
                return 억 + "억 " + String.format("%,d", 만원) + "만원";
            }
        } else {
            return String.format("%,d만원", price);
        }
    }

    public static FilterRequestDto from(Filter filter) {
        return FilterRequestDto.builder()
                .x(filter.getLongitude())
                .y(filter.getLatitude())
                .bCode(filter.getBCode())
                .hCode(filter.getHCode())
                .placeName(filter.getRegion().getCortarName())
                .tradeTypeName(filter.getTradeTypeName())
                .realEstateTypeName(filter.getRealEstateTypeName())
                .dealOrWarrantPrc(filter.getDealOrWarrantPrc())
                .rentPrice(filter.getRentPrice())
                .build();
    }

}
