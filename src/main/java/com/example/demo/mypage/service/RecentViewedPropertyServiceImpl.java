package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.exception.PropertyNotFoundException;
import com.example.demo.mypage.domain.RecentViewedProperty;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.dto.PropertyMapResponse;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.mypage.repository.RecentViewedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.dto.PropertyListItemDto;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.realty.domain.Realty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.mypage.util.MyPropertyDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RecentViewedPropertyServiceImpl implements RecentViewedPropertyService {

    private final RecentViewedPropertyRepository recentViewedPropertyRepository;
    private final UserInfoRepository userInfoRepository;
    private final PropertyRepository propertyRepository;
    private final ImageRepository imageRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void save(Long userId, Long propertyId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(PropertyNotFoundException::new);

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

        List<RecentViewedProperty> recentList = recentViewedPropertyRepository
                .findTop20ByUser_UserIdAndDeletedAtIsNullOrderByViewedAtDesc(userId);

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
        List<Property> properties = recentList.stream()
                .map(RecentViewedProperty::getProperty)
                .toList();

        List<Long> propertyIds = properties.stream()
                .map(Property::getPropertyId)
                .toList();

        Map<Long, Image> thumbnailMap = imageRepository.findThumbnailsByPropertyIds(propertyIds).stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        img -> img,
                        (v1, v2) -> v1
                ));

        Set<Long> bookmarkedPropertyIds = bookmarkedPropertyRepository
                .findBookmarkedPropertyIds(userId, propertyIds);

        return MyPropertyDtoMapper.toListDtos(properties, thumbnailMap, bookmarkedPropertyIds, 0);
    }

    @Override
    public List<PropertyExcelDto> getRecentViewedPropertiesForExcel(Long userId) {
        validateUserId(userId);
        List<RecentViewedProperty> recentViewed = recentViewedPropertyRepository.findTop20ByUser_UserIdAndDeletedAtIsNullOrderByViewedAtDesc(userId);

        List<Long> propertyIds = recentViewed.stream()
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


        return IntStream.range(0, recentViewed.size())
                .mapToObj(i -> {
                    Property p = recentViewed.get(i).getProperty();
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


}
