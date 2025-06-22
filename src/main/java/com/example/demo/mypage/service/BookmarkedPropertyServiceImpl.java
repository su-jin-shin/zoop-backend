package com.example.demo.mypage.service;

import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.realty.domain.Realty;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class BookmarkedPropertyServiceImpl implements BookmarkedPropertyService {

    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;
    private final ImageRepository imageRepository;

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MyPropertyPageResponse getBookmarkedProperties(Long userId, Pageable pageable) {
        validateUserId(userId);
        Page<BookmarkedProperty> pageResult = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId, pageable);
        return convertToResponse(userId, pageResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MapPropertyDto> getMapProperties(Long userId) {
        validateUserId(userId);
        List<BookmarkedProperty> allBookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);

        return allBookmarks.stream()
                .map(bp -> {
                    Property p = bp.getProperty();

                    return MapPropertyDto.builder()
                            .propertyId(p.getPropertyId())
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MyPropertyPageResponse getPagedProperties(Long userId, int page, int size) {
        validateUserId(userId);
        List<BookmarkedProperty> bookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);

        List<BookmarkedProperty> sorted = sortBookmarks(bookmarks, "recent"); // sort 하드코딩
        // 2️⃣ 페이지네이션 수동 처리
        int start = page * size;
        int end = Math.min(start + size, sorted.size());
        List<BookmarkedProperty> pageContent = start >= end ? List.of() : sorted.subList(start, end);

        Page<BookmarkedProperty> pageResult = new PageImpl<>(pageContent, PageRequest.of(page, size), sorted.size());
        return convertToResponse(userId, pageResult, page, size);
    }

    private MyPropertyPageResponse convertToResponse(Long userId, Page<BookmarkedProperty> pageResult) {
        return convertToResponse(userId, pageResult, pageResult.getNumber(), pageResult.getSize());
    }

    private MyPropertyPageResponse convertToResponse(Long userId, Page<BookmarkedProperty> pageResult, int page, int size) {
        List<BookmarkedProperty> content = pageResult.getContent();
        List<Long> propertyIds = content.stream()
                .map(bp -> bp.getProperty().getPropertyId())
                .toList();

        // 썸네일 매핑
        Map<Long, Image> thumbnailMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        img -> img,
                        (v1, v2) -> v1
                ));

        List<PropertyListItemDto> responseList = IntStream.range(0, content.size())
                .mapToObj(i -> {
                    Property p = content.get(i).getProperty();
                    Long pid = p.getPropertyId();
                    Image thumbnail = thumbnailMap.get(pid);

                    return PropertyListItemDto.builder()
                            .order(i + 1 + page * size)
                            .propertyId(pid)
                            .tradeTypeName(p.getTradeTypeName())
                            .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                            .rentPrice(p.getRentPrice())
                            .warrantPrice(p.getWarrantPrice())
                            .dealPrice(p.getDealPrice())
                            .summary(p.getTagList() == null ? List.of() : List.copyOf(p.getTagList()))
                            .articleName(p.getArticleName())
                            .aptName(p.getAptName())
                            .buildingName(p.getBuildingName())
                            .realEstateTypeName(p.getRealEstateTypeName())
                            .area2(p.getArea2())
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .isBookmarked(true)
                            .imageUrl(thumbnail != null ? thumbnail.getImageUrl() : null)
                            .build();
                })
                .toList();

        return new MyPropertyPageResponse(
                responseList,
                page,
                size,
                pageResult.hasNext()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyListItemDto> getAllBookmarkedPropertyResponses(Long userId) {
        validateUserId(userId);
        List<BookmarkedProperty> bookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);

        List<Long> propertyIds = bookmarks.stream()
                .map(bp -> bp.getProperty().getPropertyId())
                .toList();

        Map<Long, Image> thumbnailMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        img -> img,
                        (v1, v2) -> v1
                ));

        return IntStream.range(0, bookmarks.size())
                .mapToObj(i -> {
                    Property p = bookmarks.get(i).getProperty();
                    Long pid = p.getPropertyId();
                    Image thumbnail = thumbnailMap.get(pid);

                    return PropertyListItemDto.builder()
                            .order(i + 1)
                            .propertyId(pid)
                            .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                            .dealPrice(p.getDealPrice())
                            .warrantPrice(p.getWarrantPrice())
                            .rentPrice(p.getRentPrice())
                            .aptName(p.getAptName())
                            .area2(p.getArea2())
                            .realEstateTypeName(p.getRealEstateTypeName())
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .tradeTypeName(p.getTradeTypeName())
                            .articleName(p.getArticleName())
                            .summary(p.getTagList() == null ? List.of() : List.copyOf(p.getTagList()))
                            .imageUrl(thumbnail != null ? thumbnail.getImageUrl() : null)
                            .isBookmarked(true)
                            .build();
                })
                .toList();
    }

    @Override
    public List<PropertyExcelDto> getBookmarkedPropertiesForExcel(Long userId) {
        validateUserId(userId);
        List<BookmarkedProperty> bookmarked = bookmarkedPropertyRepository.
                findAllWithPropertyAndRealtyByUserId(userId);

        List<Long> propertyIds = bookmarked.stream()
                .map(rvp -> rvp.getProperty().getPropertyId())
                .toList();

        // 썸네일 매핑
        Map<Long, Image> thumbnailMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        img -> img,
                        (v1, v2) -> v1
                ));

        // 찜 여부
        Set<Long> bookmarkedPropertyIds = bookmarkedPropertyRepository
                .findBookmarkedPropertyIds(userId, propertyIds);


        return IntStream.range(0, bookmarked.size())
                .mapToObj(i -> {
                    Property p = bookmarked.get(i).getProperty();
                    Realty realtor = p.getRealty();
                    Long pid = p.getPropertyId();
                    Image thumbnail = thumbnailMap.get(pid);

                    PropertyExcelDto dto = PropertyExcelDto.from(p, realtor, i + 1);

                    // ✅ 추가 정보 보정
                    dto.setImageUrl(thumbnail != null ? thumbnail.getImageUrl() : null);
                    dto.setIsBookmarked(bookmarkedPropertyIds.contains(pid));
                    dto.setAptName(p.getAptName());
                    dto.setBuildingName(p.getBuildingName());
                    dto.setRealEstateTypeName(p.getRealEstateTypeName());

                    return dto;
                })
                .toList();
    }

    private List<BookmarkedProperty> sortBookmarks(List<BookmarkedProperty> bookmarks, String sortKey) {
        // 현재는 sortKey 무시하고 createdAt DESC 기준 고정
        return bookmarks.stream()
                .sorted(Comparator.comparing(BookmarkedProperty::getCreatedAt).reversed())
                .toList();
    }



}
