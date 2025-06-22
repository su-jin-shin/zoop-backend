package com.example.demo.Filter.domain.enums;

import com.example.demo.common.exception.InvalidRequestException;

public enum TradeTypeCode {
    A1(TradeTypeName.매매),
    B1(TradeTypeName.전세),
    B2(TradeTypeName.월세);

    private final TradeTypeName tradeTypeName;

    TradeTypeCode(TradeTypeName tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }



    // 거래타입 이름 -> code
    public static TradeTypeCode fromName(TradeTypeName tradeTypeName) {
        for (TradeTypeCode code : values()) {
            if (code.tradeTypeName == tradeTypeName) {
                return code;
            }
        }
        throw new InvalidRequestException();
    }

}
