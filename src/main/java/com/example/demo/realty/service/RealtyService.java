package com.example.demo.realty.service;

import com.example.demo.realty.dto.RealtyWithPropertiesResponseDto;

public interface RealtyService {
    //부동산별 매물 조회
    RealtyWithPropertiesResponseDto getRealtyWithProperties(Long realtyId, String dealType);
}
