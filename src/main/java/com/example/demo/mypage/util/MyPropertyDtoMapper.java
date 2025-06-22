package com.example.demo.mypage.util;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.dto.PropertyListItemDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class MyPropertyDtoMapper {
    public static List<PropertyListItemDto> toListDtos(List<Property> properties,
                                                       Map<Long, Image> thumbnailMap,
                                                       Set<Long> bookmarkedIds, int offset) {
    return IntStream.range(0, properties.size())
            .mapToObj(i -> {
                Property p = properties.get(i);
                Long pid = p.getPropertyId();
                Image thumbnail = thumbnailMap.get(pid);

                return PropertyListItemDto.builder()
                        .order(i + 1 + offset)
                        .propertyId(pid)
                        .articleName(p.getArticleName())
                        .aptName(p.getAptName())
                        .buildingName(p.getBuildingName())
                        .realEstateTypeName(p.getRealEstateTypeName())
                        .tradeTypeName(p.getTradeTypeName())
                        .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                        .rentPrice(p.getRentPrice())
                        .warrantPrice(p.getWarrantPrice())
                        .dealPrice(p.getDealPrice())
                        .latitude(p.getLatitude())
                        .longitude(p.getLongitude())
                        .summary(p.getTagList() == null ? List.of() : List.copyOf(p.getTagList()))
                        .area2(p.getArea2())
                        .isBookmarked(bookmarkedIds.contains(pid))
                        .imageUrl(thumbnail != null ? thumbnail.getImageUrl() : null)
                        .build();
            })
            .toList();
}
}
