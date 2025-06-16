package com.example.demo.property.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.BookmarkedPropertyRequestDto;
import com.example.demo.property.dto.BookmarkedPropertyResponseDto;
import com.example.demo.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyLikeServiceImpl implements PropertyLikeService {
    private final UserInfoRepository userInfoRepository;
    private final PropertyRepository propertyRepository;
    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;

    //매물 찜 추가
    @Override
    public BookmarkedPropertyResponseDto likeProperty(Long userId, Long propertyId) {
        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 매물 조회
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(NotFoundException::new);

        // 기존 찜 여부 확인
        BookmarkedProperty bookmark = bookmarkedPropertyRepository
                .findByUserAndProperty(userInfo, property)
                .orElseGet(() -> BookmarkedProperty.builder()
                        .user(userInfo)
                        .property(property)
                        .isBookmarked(true)
                        .build());

        bookmark.setIsBookmarked(true);
        bookmarkedPropertyRepository.save(bookmark);

        return BookmarkedPropertyResponseDto.builder()
                .propertyId(property.getPropertyId())
                .isBookmarked(true)
                .build();
    }

    //매물 매물 찜 취소
    @Override
    public BookmarkedPropertyResponseDto unlikeProperty(Long userId, Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(NotFoundException::new);

        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        BookmarkedProperty bookmarked = bookmarkedPropertyRepository.findByUserAndProperty(user,property)
                .orElseThrow(NotFoundException::new);

        bookmarked.setIsBookmarked(false);
        bookmarkedPropertyRepository.save(bookmarked);

        return BookmarkedPropertyResponseDto.builder()
                .propertyId(property.getPropertyId())
                .isBookmarked(false)
                .build();
    }
}