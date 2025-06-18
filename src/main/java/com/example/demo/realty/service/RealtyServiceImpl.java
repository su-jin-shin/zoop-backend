package com.example.demo.realty.service;

import com.example.demo.common.exception.NotFoundException;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.realty.domain.Realty;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.PropertySummaryRepository;
import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;
import com.example.demo.realty.dto.RealtyPropertyListResponseDto;
import com.example.demo.realty.repository.RealtyRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtyServiceImpl implements RealtyService {

    private final RealtyRepository realtyRepository;
    private final PropertyRepository propertyRepository;
    private final ImageRepository imageRepository;
    private final PropertySummaryRepository propertySummaryRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;


    //공인중개사 정보 조회 (부동산)
    @Override
    public RealtyAgentNumberResponseDto getRealtyAgentNumber(Long realtyId) {
        Realty realty = realtyRepository.findById(realtyId)
                .orElseThrow(NotFoundException::new);

        return RealtyAgentNumberResponseDto.of(realty);
    }

    //부동산별 매물보기 (매물)
    @Override
    public RealtyPropertyListResponseDto getPropertiesByRealty(Long realtyId, String tradeTypeName, Long userId, Pageable pageable) {
        Page<Property> propertyPage;

        // 거래 유형 필터
        if (tradeTypeName != null && !tradeTypeName.isBlank()) {
            propertyPage = propertyRepository.findByRealty_RealtyIdAndTradeTypeName(realtyId, tradeTypeName, pageable);
        } else {
            propertyPage = propertyRepository.findByRealty_RealtyId(realtyId, pageable);
        }

        // 매물 ID 리스트 추출
        List<Long> propertyIds = propertyPage.getContent()
                .stream()
                .map(Property::getPropertyId)
                .toList();

        // 요약 정보 가져오기
        List<PropertySummary> summaries = propertySummaryRepository.findByProperty_PropertyIdIn(propertyIds);

        // 썸네일 이미지 가져오기
        List<Image> thumbnailImages = imageRepository.findThumbnailsByPropertyIds(propertyIds);
        Map<Long, Image> thumbnailMap = thumbnailImages.stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        img -> img
                ));

        //  찜한 매물 ID 조회
        Set<Long> bookmarkedPropertyIds = (userId != null)
                ? bookmarkedPropertyRepository.findBookmarkedPropertyIds(userId, propertyIds)
                : Set.of();

        // DTO 변환
        List<PropertyListItemDto> propertyDtos = new ArrayList<>();
        int order = 1 + (propertyPage.getNumber() * propertyPage.getSize());

        for (int i = 0; i < propertyPage.getContent().size(); i++) {
            Property property = propertyPage.getContent().get(i);
            PropertySummary summary = (i < summaries.size()) ? summaries.get(i) : null;
            Image thumbnail = thumbnailMap.get(property.getPropertyId());
            List<Image> imageList = (thumbnail != null) ? List.of(thumbnail) : List.of();

            boolean isBookmarked = bookmarkedPropertyIds.contains(property.getPropertyId());

            PropertyListItemDto dto = PropertyListItemDto.of(
                    property,
                    summary,
                    imageList,
                    isBookmarked,
                    order++
            );

            propertyDtos.add(dto);
        }

        return RealtyPropertyListResponseDto.builder()
                .properties(propertyDtos)
                .page(propertyPage.getNumber())
                .size(propertyPage.getSize())
                .hasNext(propertyPage.hasNext())
                .build();
    }


}
