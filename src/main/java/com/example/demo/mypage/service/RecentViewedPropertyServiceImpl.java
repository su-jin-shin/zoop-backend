package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.mypage.domain.RecentViewedProperty;
import com.example.demo.mypage.dto.RecentViewedPropertyResponse;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.mypage.repository.RecentViewedPropertyRepository;
import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.repository.ImageRepository;
import com.example.demo.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                                    .build();
                            recentViewedPropertyRepository.save(newOne);
                        }
                );
    }

    @Override
    public List<RecentViewedPropertyResponse> getRecentViewedList(Long userId) {

        log.info("📌 [getRecentViewedList] userId = {}", userId); // ✅ 2️⃣ 서비스 진입 확인

        List<RecentViewedProperty> recentList = recentViewedPropertyRepository
                .findTop20ByUser_UserIdOrderByViewedAtDesc(userId);

        log.info("📌 최근 본 매물 수 = {}", recentList.size()); // ✅ 3️⃣ 매물이 DB에서 나오는지 확인

        if (recentList.isEmpty()) {
            log.warn("⚠️ 최근 본 매물이 없습니다.");
        }

        List<Long> propertyIds = recentList.stream()
                .map(rvp -> rvp.getProperty().getPropertyId())
                .toList();

        // ✳ 대표 이미지 조회 (propertyId -> imageUrl)
        Map<Long, String> thumbnailMap = imageRepository
                .findTopImagesByPropertyIds(propertyIds)
                .stream()
                .collect(Collectors.toMap(
                        img -> img.getProperty().getPropertyId(),
                        Image::getImageUrl,
                        (v1, v2) -> v1 // 중복 시 첫 번째
                ));

        // ✳ 찜 여부 조회 (propertyId -> true)
        Set<Long> bookmarkedPropertyIds = bookmarkedPropertyRepository
                .findBookmarkedPropertyIds(userId, propertyIds);

        log.info("🖼️ 이미지 맵 size = {}", thumbnailMap.size());
        log.info("❤️ 찜한 매물 ID 수 = {}", bookmarkedPropertyIds.size());

        return IntStream.range(0, recentList.size())
                .mapToObj(i -> {
                    RecentViewedProperty entity = recentList.get(i);
                    Property p = entity.getProperty();
                    Long pid = p.getPropertyId();

                    return RecentViewedPropertyResponse.builder()
                            .order(i + 1)
                            .propertyId(pid)
                            .articleName(p.getArticleName())
                            .aptName("아파트".equals(p.getRealEstateTypeName()) || "오피스텔".equals(p.getRealEstateTypeName()) ? p.getArticleName() : p.getAptName())
                            .buildingName(p.getBuildingName())
                            .realestateTypeName(p.getRealEstateTypeName())
                            .tradeTypeName(p.getTradeTypeName())
                            .dealOrWarrantPrc(p.getDealOrWarrantPrc())
                            .rentPrice(p.getRentPrice())
                            .warrantPrice(p.getWarrantPrice())
                            .dealPrice(p.getDealPrice())
                            .summary(p.getTagList())
                            .area1(p.getArea1())
                            .isBookmarked(bookmarkedPropertyIds.contains(pid))
                            .isActive(!"Y".equalsIgnoreCase(p.getTradeCompleteYN()))
                            .thumbnailImage(thumbnailMap.get(pid))
                            .build();
                })
                .toList();
    }
}
