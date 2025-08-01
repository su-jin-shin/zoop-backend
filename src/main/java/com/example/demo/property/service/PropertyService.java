package com.example.demo.property.service;

import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.*;
import com.example.demo.property.dto.RealtyWithPropertiesResponseDto;
import com.example.demo.property.dto.RealtyWithPropertiesResponseDto;




import java.util.List;
import java.util.Map;

public interface PropertyService {

    //매물 상세 조회( 기본정보) usertId 없이 호출
    PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId);

    //매물 상세조회 (기본정보)
    PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId,Long userId);



    //매물 상세조회 (상세 설명)
    PropertyDescriptionResponseDto getPropertyDescription(Long propertyId);

    //매물 상세 조회 (시설정보)
    PropertyFacilitiesResponseDto getPropertyFacilities(Long propertyId);

    //매물 상세 조회 (위치정보)
    PropertyLocationResponseDto getPropertyLocation(Long propertyId);

    //매물 상세 조회 (거래 정보)
    PropertyTransactionResponseDto getPropertyTransaction(Long propertyId);

    //매물 상세 조회 (중개 정보)
    PropertyAgentResponseDto getPropertyAgent(Long propertyId);

    //매물 상세 조회 (중개 보수 및 세금정보)
    PropertyBrokerFeeResponseDto getBrokerFee(Long propertyId);

    //매물 상세 조회 (매물 정보)
    PropertyPropertyInfoResponseDto getPropertyInfo(Long propertyId);

    //공인중개사 연락처 조회 (매물 상세페이지)
    PropertyAgentNumberResponseDto getPropertyAgentNumber(Long propertyId);

    //매물 비교 하기
    List<PropertyCompareResponseDto> getCompareProperties(List<Long> propertyIds);

    //매물 OG 조회
    PropertyOgResponseDto getPropertyOg(Long propertyId);

    //부동산 별 매물 (부동산 정보) 조회
    RealtyWithPropertiesResponseDto getRealtyWithProperties(Long propertyId);

    //ai가 추천해준 매물의 번호(articleNo)를 통해 매물 id를 조회
    Property findByArticleNo(String articleNo);

    //ai의 매물 요약 저장
    void updateAiMessage(Long propertyId, Map<String, List<String>> summary);

}
