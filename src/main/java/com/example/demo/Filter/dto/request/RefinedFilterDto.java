package com.example.demo.Filter.dto.request;

import com.example.demo.Filter.domain.enums.TradeTypeCode;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@ToString
public class RefinedFilterDto {

    private String regionName;
    private String regionCode;

    private String tradeTypeName;
    private String tradeTypeCode;

    private String realEstateTypeName;
    private String realEstateTypeCode;

    private int dealOrWarrantPrc;
    private int rentPrice;

    private static final Map<String, String> ESTATE_TYPE_CODE_MAP = Map.of(
            "아파트", "APT",
            "오피스텔", "OPST",
            "빌라", "VL:YR",
            "원룸_투룸", "DDDGG:DSD"
    );

    private static String getRealEstateTypeCode(String name) {
        return Optional.ofNullable(ESTATE_TYPE_CODE_MAP.get(name))
                .orElseThrow(() -> new IllegalArgumentException("Unknown estate type name: " + name));
    }

    public static RefinedFilterDto of(FilterRequestDto filterRequestDto) {
        if (filterRequestDto.getRealEstateTypeName().isEmpty()) {
            throw new IllegalArgumentException("매물 타입이 비어있습니다.");
        }

        RefinedFilterDto dto = new RefinedFilterDto();
        dto.regionCode = filterRequestDto.getBCode();
        dto.regionName = filterRequestDto.getPlaceName();
        dto.tradeTypeName = filterRequestDto.getTradeTypeName().toString();
        dto.tradeTypeCode = TradeTypeCode.fromName(filterRequestDto.getTradeTypeName()).name();
        dto.realEstateTypeName = filterRequestDto.getRealEstateTypeName().get(0);
        dto.realEstateTypeCode = getRealEstateTypeCode(dto.realEstateTypeName);

        try {
            dto.dealOrWarrantPrc = Integer.parseInt(filterRequestDto.getDealOrWarrantPrc());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("가격 형식이 잘못되었습니다.", e);
        }

        dto.rentPrice = filterRequestDto.getRentPrice().intValue();
        return dto;
    }

}
