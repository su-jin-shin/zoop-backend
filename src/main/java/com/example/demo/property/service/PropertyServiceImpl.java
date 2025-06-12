package com.example.demo.property.service;

import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.dto.PropertyBasicInfoResponseDto;
import com.example.demo.property.dto.PropertyDescriptionResponseDto;
import com.example.demo.property.dto.PropertyFacilitiesResponseDto;
import com.example.demo.property.dto.PropertyLocationResponseDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.PropertySummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final PropertySummaryRepository summaryRepository;
    private final ImageRepository imageRepository;



    //매물 상세조회 (기본 정보)
    @Override
    public PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return null; // 또는 기본 응답 객체를 리턴하거나 예외처리
        }
        // Optional을 사용해 summary 처리
        PropertySummary summary = summaryRepository.findByProperty_PropertyId(propertyId)
                .orElse(null); // null 허용

        //매물 아이디 통해 이미지 조회
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        return PropertyBasicInfoResponseDto.of(property, summary, images);
    }




    //매물 상세조회 (상세 설명)
    @Override
    public PropertyDescriptionResponseDto getPropertyDescription(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyDescriptionResponseDto.of(property);
    }

    //매물 상세조회 (시설정보)
    @Override
    public PropertyFacilitiesResponseDto getPropertyFacilities(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyFacilitiesResponseDto.of(property);
    }

    //매물 상세조회 (위치정보)
    @Override
    public PropertyLocationResponseDto getPropertyLocation(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyLocationResponseDto.of(property);
    }
}
