package com.example.demo.property.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.dto.*;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.PropertySummaryRepository;
import com.example.demo.property.util.PropertyDtoConverter;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl  implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final PropertySummaryRepository summaryRepository;
    private final ImageRepository imageRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;
    private final UserInfoRepository userInfoRepository;



    //매물 상세조회 (기본정보) userId없는 버전
    @Override
    public PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId){
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        //Optional을 사용해 summary처리
        PropertySummary summary = summaryRepository.findByProperty_PropertyId(propertyId).orElse(null);

        //매물 아이디 통해 이미지 조회;
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        return PropertyBasicInfoResponseDto.of(property,summary,images);
    }

    //매물 상세조회 (기본 정보)
    @Override
    public PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId, Long userId) {


        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);
        PropertySummary summary = summaryRepository.findByProperty_PropertyId(propertyId).orElse(null);
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        boolean isBookmarked = false;
        if (userId != null) {
            isBookmarked = bookmarkedPropertyRepository.existsByUser_UserIdAndProperty_PropertyIdAndIsBookmarkedTrue(userId, propertyId);
        }

        return PropertyBasicInfoResponseDto.of(property, summary, images, isBookmarked);
    }




    //매물 상세조회 (상세 설명)
    @Override
    public PropertyDescriptionResponseDto getPropertyDescription(Long propertyId) {

        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyDescriptionResponseDto.of(property);
    }

    //매물 상세조회 (매물 정보)
    @Override
    public PropertyPropertyInfoResponseDto getPropertyInfo(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        //매물 아이디 통해 이미지 조회
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        return  PropertyPropertyInfoResponseDto.of(property,images);
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

    //매물 상세조회 (거래 정보)
    @Override
    public PropertyTransactionResponseDto getPropertyTransaction(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return  PropertyTransactionResponseDto.of(property);
    }

    //매물 상세조회 (중개 정보)
    @Override
    public PropertyAgentResponseDto getPropertyAgent(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyAgentResponseDto.of(property);
    }

    //매물 상세조회 (중개보수 및 세금정보)
    @Override
    public PropertyBrokerFeeResponseDto getBrokerFee(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);


        return PropertyBrokerFeeResponseDto.of(property);
    }


    //공인중개사 연락처 조회 (매물 상세페이지)
    @Override
    public PropertyAgentNumberResponseDto getPropertyAgentNumber(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        return PropertyAgentNumberResponseDto.of(property);
    }

    @Override
    public List<PropertyCompareResponseDto> getCompareProperties(List<Long> propertyIds) {
        List<PropertyCompareResponseDto> result = new ArrayList<>();

        List<Property> properties = propertyRepository.findAllById(propertyIds);
        List<Image> allImages = imageRepository.findByProperty_PropertyIdIn(propertyIds);

        //이미지 매핑
        Map<Long,String> thumbnailMap = new HashMap<>();
        for (Image image : allImages) {
            Long propertyId = image.getProperty().getPropertyId();
            if(Boolean.TRUE.equals(image.getIsMain()) && !thumbnailMap.containsKey(propertyId)){
                thumbnailMap.put(propertyId, image.getImageUrl());
            }
        }
        //태그매핑
        Map<Long, List<String>> tagMap = new HashMap<>();
        for (Long propertyId : propertyIds) {
            Optional<PropertySummary> summaryOpt = summaryRepository.findByProperty_PropertyId(propertyId);
            if (summaryOpt.isPresent()) {
                List<String> tags = PropertyDtoConverter.convertSummary(summaryOpt.get());
                tagMap.put(propertyId, tags);
            } else {
                tagMap.put(propertyId, new ArrayList<>());
            }
        }
        //비교 dto 변환
        for (Property p : properties){
            PropertyCompareResponseDto dto = PropertyCompareResponseDto.builder()
                    .propertyId(p.getPropertyId())
                    .imageUrl(thumbnailMap.getOrDefault(p.getPropertyId(), null))
                    .tradeTypeName(p.getTradeTypeName())
                    .dealPrice(p.getDealPrice())
                    .rentPrice(p.getRentPrice())
                    .warrantPrice(p.getWarrantPrice())
                    .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                    .articleName(p.getArticleName())
                    .useApproveYmd(p.getUseApproveYmd())
                    .tagList(tagMap.getOrDefault(p.getPropertyId(), new ArrayList<>()))
                    .area1(p.getArea1())
                    .area2(p.getArea2())
                    .roomCount(p.getRoomCount())
                    .bathroomCount(p.getBathroomCount())
                    .floorInfo(p.getFloorInfo())
                    .directionBaseTypeName(p.getDirectionBaseTypeName())
                    .direction(p.getDirection())
                    .etcFeeAmount(p.getEtcFeeAmount())
                    .moveInPossibleYmd(p.getMoveInPossibleYmd())
                    .parkingPossibleYN(p.getParkingPossibleYN())
                    .securityFacilities(p.getSecurityFacilities())
                    .build();

            result.add(dto);
        }
        return result;
    }


}
