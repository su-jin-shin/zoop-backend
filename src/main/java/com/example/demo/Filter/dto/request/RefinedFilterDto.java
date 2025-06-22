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

    private String getRealEstateTypeCode(String name) {
        return Optional.ofNullable(ESTATE_TYPE_CODE_MAP.get(name))
                .orElseThrow(() -> new IllegalArgumentException("Unknown estate type name: " + name));
    }

    public RefinedFilterDto(FilterRequestDto filterRequestDto) {
        this.regionCode = filterRequestDto.getBCode();
        System.out.println("regionCode: " +  regionCode);
        this.regionName = filterRequestDto.getPlaceName();
        this.tradeTypeName = filterRequestDto.getTradeTypeName().toString();
        this.tradeTypeCode = TradeTypeCode.fromName(filterRequestDto.getTradeTypeName()).name();

        if (filterRequestDto.getRealEstateTypeName().isEmpty()) {
            throw new IllegalArgumentException("매물 타입이 비어있습니다.");
        }

        this.realEstateTypeName = filterRequestDto.getRealEstateTypeName().get(0);
        this.realEstateTypeCode = getRealEstateTypeCode(realEstateTypeName);

        try {
            this.dealOrWarrantPrc = Integer.parseInt(filterRequestDto.getDealOrWarrantPrc());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("가격 형식이 잘못되었습니다.", e);
        }

        this.rentPrice = filterRequestDto.getRentPrice().intValue();
    }

}
