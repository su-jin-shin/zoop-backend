package com.example.demo.property.service;

import com.example.demo.property.dto.PropertyBasicInfoResponseDto;
import com.example.demo.property.dto.PropertyDescriptionResponseDto;
import com.example.demo.property.dto.PropertyFacilitiesResponseDto;
import com.example.demo.property.dto.PropertyLocationResponseDto;

public interface PropertyService {
    //매물 상세조회 (기본 정보)
    PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId);

    //매물 상세조회 (상세 설명)
    PropertyDescriptionResponseDto getPropertyDescription(Long propertyId);

    //매물 상세 조회 (시설정보)
    PropertyFacilitiesResponseDto getPropertyFacilities(Long propertyId);

    //매물 상세 조회 (위치정보)
    PropertyLocationResponseDto getPropertyLocation(Long propertyId);

}
