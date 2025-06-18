package com.example.demo.realty.service;

import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;
import com.example.demo.realty.dto.RealtyPropertyListResponseDto;


import org.springframework.data.domain.Pageable;


public interface RealtyService {



    //공인중개사 연락처 조회 (부동산 상세 페이지)
    RealtyAgentNumberResponseDto getRealtyAgentNumber(Long realtyId);

    //부동산 별 매물 조회 (매물 리스트)
    RealtyPropertyListResponseDto getPropertiesByRealty(Long realtyId,String tradeTypeName, Long userId,Pageable pageable);
}
