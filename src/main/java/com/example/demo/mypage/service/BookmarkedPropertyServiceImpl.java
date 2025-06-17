package com.example.demo.mypage.service;

import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.dto.BookmarkedPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.ImageDto;
import com.example.demo.property.dto.PropertyListItemDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.realty.domain.Realty;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkedPropertyServiceImpl implements BookmarkedPropertyService {

    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    public BookmarkedPropertyPageResponse getBookmarkedProperties(Long userId, Pageable pageable) {
        // 마이페이지에서는 정렬 조건 무시하고 createdAt DESC
        Page<BookmarkedProperty> pageResult = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId, pageable);
        return convertToResponse(userId, pageResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MapPropertyDto> getMapProperties(Long userId) {
        List<BookmarkedProperty> allBookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);
        log.info("✅ 유저 {} 의 찜한 매물 수 = {}", userId, allBookmarks.size());

        return allBookmarks.stream()
                .map(bp -> {
                    Property p = bp.getProperty();
                    log.info("📍 매물 ID = {}, 위도 = {}, 경도 = {}", p.getPropertyId(), p.getLatitude(), p.getLongitude());
                    return MapPropertyDto.builder()
                            .propertyId(p.getPropertyId())
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .build();
                })
                .toList();
    }

//    정렬키: price_asc, price_desc, area_asc, area_desc, recent
    @Override
    @Transactional(readOnly = true)
    public BookmarkedPropertyPageResponse getPagedProperties(Long userId, int page, int size, String sort) {
        List<BookmarkedProperty> bookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);
        log.info("List<BookmarkedProperty> bookmarks" + bookmarks);
        // 1️⃣ 정렬 처리 (Java 쪽에서)
        List<BookmarkedProperty> sorted = sortBookmarks(bookmarks, sort);
        log.info("sorted" + sorted);
        // 2️⃣ 페이지네이션 수동 처리
        int start = page * size;
        int end = Math.min(start + size, sorted.size());
        List<BookmarkedProperty> pageContent = start >= end ? List.of() : sorted.subList(start, end);
        log.info("pageContent " + pageContent);
        Page<BookmarkedProperty> pageResult = new PageImpl<>(pageContent, PageRequest.of(page, size), sorted.size());
        log.info("pageResult " + pageResult);
        return convertToResponse(userId, pageResult, page, size);
    }

    private List<BookmarkedProperty> sortBookmarks(List<BookmarkedProperty> bookmarks, String sortKey) {
        Comparator<BookmarkedProperty> comparator = switch (sortKey) {
            case "price_asc", "price_desc" -> Comparator.comparing(bp -> {
                Property p = bp.getProperty();
                return switch (p.getTradeTypeName()) {
                    case "월세" -> p.getRentPrice();
                    case "전세" -> p.getWarrantPrice();
                    case "매매" -> p.getDealPrice();
                    default -> BigDecimal.ZERO;
                };
            }, Comparator.nullsLast(Comparator.naturalOrder()));
            case "area_asc", "area_desc" -> Comparator.comparing(bp -> bp.getProperty().getArea2());
            default -> Comparator.comparing(BookmarkedProperty::getCreatedAt).reversed();
        };

        if (sortKey.endsWith("_desc")) {
            comparator = comparator.reversed();
        }

        return bookmarks.stream().sorted(comparator).toList();
    }


    private BookmarkedPropertyPageResponse convertToResponse(Long userId, Page<BookmarkedProperty> pageResult) {
        return convertToResponse(userId, pageResult, pageResult.getNumber(), pageResult.getSize());
    }

    private BookmarkedPropertyPageResponse convertToResponse(Long userId, Page<BookmarkedProperty> pageResult, int page, int size) {
        List<BookmarkedProperty> content = pageResult.getContent();
        List<Long> propertyIds = content.stream()
                .map(bp -> bp.getProperty().getPropertyId())
                .toList();

        Map<Long, String> imageMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        Image::getImageUrl,
                        (v1, v2) -> v1
                ));

        List<PropertyListItemDto> responseList = IntStream.range(0, content.size())
                .mapToObj(i -> {
                    Property p = content.get(i).getProperty();
                    Long pid = p.getPropertyId();
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
                            .thumbnail(imageMap.get(pid) == null ? null : ImageDto.builder()
                                    .imageUrl(imageMap.get(pid))
                                    .imageType("THUMBNAIL")
                                    .imageOrder(0)
                                    .isMain(true)
                                    .build())
                            .build();
                })
                .toList();

        return new BookmarkedPropertyPageResponse(
                responseList,
                page,
                size,
                pageResult.hasNext()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyListItemDto> getAllBookmarkedPropertyResponses(Long userId) {
        List<BookmarkedProperty> bookmarks = bookmarkedPropertyRepository.findAllWithPropertyByUserId(userId);

        List<Long> propertyIds = bookmarks.stream()
                .map(bp -> bp.getProperty().getPropertyId())
                .toList();

        Map<Long, String> imageMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        Image::getImageUrl,
                        (v1, v2) -> v1
                ));

        return IntStream.range(0, bookmarks.size())
                .mapToObj(i -> {
                    Property p = bookmarks.get(i).getProperty();
                    Long pid = p.getPropertyId();
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
                            .thumbnail(imageMap.get(pid) == null ? null : ImageDto.builder()
                                    .imageUrl(imageMap.get(pid))
                                    .imageType("THUMBNAIL")
                                    .imageOrder(0)
                                    .isMain(true)
                                    .build())
                            .isBookmarked(true)
                            .build();
                })
                .toList();
    }

    @Override
    public List<PropertyExcelDto> getBookmarkedPropertiesForExcel(Long userId) {
        List<BookmarkedProperty> bookmarked = bookmarkedPropertyRepository.
                findAllWithPropertyAndRealtyByUserId(userId);

        return IntStream.range(0, bookmarked.size())
                .mapToObj(i -> {
                    Property property = bookmarked.get(i).getProperty();
                    Realty realtor = property.getRealty(); // 연관된 중개사
                    return PropertyExcelDto.from(property, realtor, i + 1);
                })
                .toList();
    }



}
