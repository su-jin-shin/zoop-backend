package com.example.demo.property.service;

import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.repository.FilterRepository;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.domain.RecommendedProperty;
import com.example.demo.property.dto.*;



import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.PropertySummaryRepository;

import com.example.demo.property.util.PropertyDtoConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl  implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final PropertySummaryRepository summaryRepository;
    private final ImageRepository imageRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;
    private final UserInfoRepository userInfoRepository;
    private final FilterRepository filterRepository;
    private final ObjectMapper objectMapper;


    //매물 상세조회 (기본정보) userId없는 버전
    @Override
    public PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId){
        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);



        //매물 아이디 통해 이미지 조회;
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        return PropertyBasicInfoResponseDto.of(property,images);
    }

    //매물 상세조회 (기본 정보)
    @Override
    public PropertyBasicInfoResponseDto getPropertyBasicInfo(Long propertyId, Long userId) {


        Property property = propertyRepository.findById(propertyId).orElseThrow(NotFoundException::new);

        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);

        boolean isBookmarked = false;
        if (userId != null) {
            isBookmarked = bookmarkedPropertyRepository.existsByUser_UserIdAndProperty_PropertyIdAndIsBookmarkedTrue(userId, propertyId);
        }


        return PropertyBasicInfoResponseDto.of(property,  images, isBookmarked);
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

    //매물 비교
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

    //매물OG 조회
    @Override
    public PropertyOgResponseDto getPropertyOg(Long propertyId) {
    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(NotFoundException::new);

    //이미지 1개
        List<Image> images = imageRepository.findThumbnailsByPropertyIds(List.of(propertyId));
        List<Image> selectedImage = images.isEmpty() ? List.of() : List.of(images.get(0));

        return PropertyOgResponseDto.of(property,selectedImage);
    }

    //부동산별 매물보기(부동산정보)
    @Override
    public RealtyWithPropertiesResponseDto getRealtyWithProperties(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(NotFoundException::new);

        Long realtyId = property.getRealty().getRealtyId();

        List<Property> properties = propertyRepository.findByRealty_RealtyId(realtyId);

        Map<String, Long> tradeTypeCountMap = properties.stream()
                .collect(Collectors.groupingBy(Property::getTradeTypeName, Collectors.counting()));

        int dealCount = tradeTypeCountMap.getOrDefault("매매", 0L).intValue();
        int leaseCount = tradeTypeCountMap.getOrDefault("전세", 0L).intValue();
        int rentCount = tradeTypeCountMap.getOrDefault("월세", 0L).intValue();

        return RealtyWithPropertiesResponseDto.of(property, dealCount, leaseCount, rentCount);
    }

    // ai가 추천해준 매물의 번호(articleNo)를 통해 매물 id를 조회
    public Property findByArticleNo(String articleNo) {
        Optional<Property> optionalProperty = propertyRepository.findByArticleNo(articleNo);
        return optionalProperty.orElse(null); // 매물테이블에 추천 받은 매물의 정보가 존재하지 않으면 null 반환
    }

    // ai 요약 update
    @Transactional
    public void updateAiMessage(Long propertyId,
                                Map<String, List<String>> summary) {

        try {
            String json = objectMapper.writeValueAsString(summary);
            propertyRepository.updateAiSummaryNative(propertyId, json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("aiSummary 직렬화 실패", e);
        }
    }

}
