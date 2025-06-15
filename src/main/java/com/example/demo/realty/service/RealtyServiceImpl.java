package com.example.demo.realty.service;

import com.example.demo.common.exception.NotFoundException;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import com.example.demo.property.domain.enums.ImageType;
import com.example.demo.property.dto.ImageDto;
import com.example.demo.property.dto.PropertyListItemDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.PropertySummaryRepository;
import com.example.demo.property.util.PropertyDtoConverter;
import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;
import com.example.demo.realty.dto.RealtyWithPropertiesResponseDto;
import com.example.demo.realty.repository.RealtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealtyServiceImpl implements RealtyService {

    private final RealtyRepository realtyRepository;
    private final PropertyRepository propertyRepository;
    private final ImageRepository imageRepository;
    private final PropertySummaryRepository propertySummaryRepository;

    @Override
    public RealtyWithPropertiesResponseDto getRealtyWithProperties(Long realtyId, String dealType) {
        // 1. 부동산 조회
        Realty realty = realtyRepository.findById(realtyId)
                .orElseThrow(NotFoundException::new);

        // 2. 매물 전체 조회
        List<Property> properties = propertyRepository.findByRealty_RealtyId(realtyId);



        properties.forEach(p -> System.out.println(" - " + p.getTradeTypeName()));


        // 2-1. 거래유형 필터링 (dealType = "월세", "전세", "매매")
        if (dealType != null && !dealType.isBlank()) {
            String normalized = dealType.trim();

            properties = properties.stream()
                    .filter(p -> {
                        String tradeType = p.getTradeTypeName();
                        boolean match = tradeType != null && tradeType.trim().equalsIgnoreCase(normalized);

                        return match;
                    })
                    .toList();
        }

        // 3. 필터링된 매물 ID 추출
        List<Long> propertyIds = properties.stream()
                .map(Property::getPropertyId)
                .toList();

        // 4. 이미지 일괄 조회 및 매핑
        List<Image> allImages = imageRepository.findByProperty_PropertyIdIn(propertyIds);
        Map<Long, List<Image>> imageMap = allImages.stream()
                .collect(Collectors.groupingBy(img -> img.getProperty().getPropertyId()));

        // 5. 요약 일괄 조회 및 매핑
        Map<Long, List<String>> summaryMap = propertyIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> propertySummaryRepository.findByProperty_PropertyId(id)
                                .map(PropertyDtoConverter::convertSummary)
                                .orElse(Collections.emptyList())
                ));

        // 6. DTO 변환
        List<PropertyListItemDto> propertyList = properties.stream()
                .map(p -> {
                    List<Image> allPropertyImages = imageMap.getOrDefault(p.getPropertyId(), Collections.emptyList());

                    // 썸네일 추출 우선순위: PROPERTY > COMPLEX > STRUCTURE
                    ImageDto thumbnail = allPropertyImages.stream()
                            .filter(img -> img.getImageType() == ImageType.PROPERTY && Boolean.TRUE.equals(img.getIsMain()))
                            .findFirst()
                            .or(() -> allPropertyImages.stream()
                                    .filter(img -> img.getImageType() == ImageType.COMPLEX && Boolean.TRUE.equals(img.getIsMain()))
                                    .findFirst())
                            .or(() -> allPropertyImages.stream()
                                    .filter(img -> img.getImageType() == ImageType.STRUCTURE && Boolean.TRUE.equals(img.getIsMain()))
                                    .findFirst())
                            .map(ImageDto::from)
                            .orElse(null);

                    return PropertyListItemDto.builder()
                            .propertyId(p.getPropertyId())
                            .tradeTypeName(p.getTradeTypeName())
                            .rentPrice(p.getRentPrice())
                            .warrantPrice(p.getWarrantPrice())
                            .dealPrice(p.getDealPrice())
                            .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                            .summary(summaryMap.getOrDefault(p.getPropertyId(), Collections.emptyList()))
                            .aptName(p.getAptName())
                            .buildingName(p.getBuildingName())
                            .realestateTypeName(p.getRealEstateTypeName())
                            .area2(p.getArea2())
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .thumbnail(thumbnail)
                            .articleName(p.getArticleName())
                            .build();
                })
                .collect(Collectors.toList());

        // 7. 최종 응답 조립
        return RealtyWithPropertiesResponseDto.builder()
                .realtyId(realty.getRealtyId())
                .realtorName(realty.getRealtorName())
                .establishRegistrationNo(realty.getEstablishRegistrationNo())
                .address(realty.getAddress())
                .dealCount(realty.getDealCount())
                .leaseCount(realty.getLeaseCount())
                .rentCount(realty.getRentCount())
                .representativeName(realty.getRepresentativeName())
                .propertie(propertyList)
                .build();
    }

    //공인중개사 정보 조회 (부동산)
    @Override
    public RealtyAgentNumberResponseDto getRealtyAgentNumber(Long realtyId) {
        Realty realty = realtyRepository.findById(realtyId)
                .orElseThrow(NotFoundException::new);

        return RealtyAgentNumberResponseDto.of(realty);
    }
}
