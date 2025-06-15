package com.example.demo.realty.service;

import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;
import com.example.demo.realty.dto.RealtyWithPropertiesResponseDto;

public interface RealtyService {
    //부동산별 매물 조회
    RealtyWithPropertiesResponseDto getRealtyWithProperties(Long realtyId, String dealType);

    //공인중개사 연락처 조회 (부동산 상세 페이지)
    RealtyAgentNumberResponseDto getRealtyAgentNumber(Long realtyId);
}
