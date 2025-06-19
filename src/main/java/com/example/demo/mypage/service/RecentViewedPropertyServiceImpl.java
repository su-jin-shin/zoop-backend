package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.domain.RecentViewedProperty;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.dto.PropertyMapResponse;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.mypage.repository.RecentViewedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.ImageDto;
import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.realty.domain.Realty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.property.domain.QProperty.property;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentViewedPropertyServiceImpl implements RecentViewedPropertyService {

    private final RecentViewedPropertyRepository recentViewedPropertyRepository;
    private final UserInfoRepository userInfoRepository;
    private final PropertyRepository propertyRepository;
    private final ImageRepository imageRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;

    @Override
    public void save(Long userId, Long propertyId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("매물을 찾을 수 없습니다."));

        recentViewedPropertyRepository.findByUserAndProperty(user, property)
                .ifPresentOrElse(
                        existing -> {
                            existing.setViewedAt(LocalDateTime.now());
                            recentViewedPropertyRepository.save(existing);
                        },
                        () -> {
                            RecentViewedProperty newOne = RecentViewedProperty.builder()
                                    .user(user)
                                    .property(property)
                                    .viewedAt(LocalDateTime.now())
                                    .build();
                            recentViewedPropertyRepository.save(newOne);
                        }
                );
    }

    @Override
    public List<PropertyListItemDto> getRecentViewedList(Long userId) {

        log.info("📌 [getRecentViewedList] userId = {}", userId); // ✅ 2️⃣ 서비스 진입 확인

        List<RecentViewedProperty> recentList = recentViewedPropertyRepository
                .findTop20ByUser_UserIdAndDeletedAtIsNullOrderByViewedAtDesc(userId);

        log.info("📌 최근 본 매물 수 = {}", recentList.size()); // ✅ 3️⃣ 매물이 DB에서 나오는지 확인

        if (recentList.isEmpty()) {
            log.warn("⚠️ 최근 본 매물이 없습니다.");
        }
        return convertToListDtos(userId, recentList);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyMapResponse getRecentViewedPropertiesWithMap(Long userId){

        List<RecentViewedProperty> recentList =
                recentViewedPropertyRepository.findTop20ByUser_UserIdAndDeletedAtIsNullOrderByViewedAtDesc(userId);

        List<PropertyListItemDto> listDtos = convertToListDtos(userId, recentList);

        List<MapPropertyDto> mapDtos = listDtos.stream()
                .map(dto -> MapPropertyDto.builder()
                        .propertyId(dto.getPropertyId())
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .build())
                .toList();

        return PropertyMapResponse.builder()
                .mapProperties(mapDtos)
                .myPropertyPageResponse(new MyPropertyPageResponse(
                        listDtos, 0, listDtos.size(), false
                ))
                .build();
    }

    private List<PropertyListItemDto> convertToListDtos(Long userId, List<RecentViewedProperty> recentList) {
        List<Long> propertyIds = recentList.stream()
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

        return IntStream.range(0, recentList.size())
                .mapToObj(i -> {
                    RecentViewedProperty entity = recentList.get(i);
                    Property p = entity.getProperty();
                    Long pid = p.getPropertyId();
                    Image thumbnail = thumbnailMap.get(pid);

                    return PropertyListItemDto.builder()
                            .order(i + 1)
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
                            .isBookmarked(bookmarkedPropertyIds.contains(pid))
                            .imageUrl(thumbnail != null ? thumbnail.getImageUrl() : null)
                            .build();
                })
                .toList();
    }


    @Override
    public List<PropertyExcelDto> getRecentViewedPropertiesForExcel(Long userId) {
        List<RecentViewedProperty> recentViewed = recentViewedPropertyRepository.findTop20ByUser_UserIdAndDeletedAtIsNullOrderByViewedAtDesc(userId);

        return IntStream.range(0, recentViewed.size())
                .mapToObj(i -> {
                    Property property = recentViewed.get(i).getProperty();
                    Realty realtor = property.getRealty(); // 연관된 중개사
                    return PropertyExcelDto.from(property, realtor, i + 1);
                })
                .toList();
    }
}
