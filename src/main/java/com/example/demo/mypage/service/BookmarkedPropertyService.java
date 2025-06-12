package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Property;
import com.example.demo.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookmarkedPropertyService {

    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;
    private final PropertyRepository propertyRepository;
    private final UserInfoRepository userInfoRepository;

    public void toggleBookmark(Long userId, Long propertyId, Boolean isBookmarked) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매물입니다."));

        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        bookmarkedPropertyRepository.findByUserAndProperty(user, property)
                .ifPresentOrElse(
                        existing -> {
                            existing.setIsBookmarked(isBookmarked);
                            // updatedAt은 @UpdateTimestamp로 자동 업데이트도 가능
                        },
                        () -> {
                            BookmarkedProperty newBookmark = BookmarkedProperty.builder()
                                    .user(user)
                                    .property(property)
                                    .isBookmarked(isBookmarked)
                                    .build();
                            bookmarkedPropertyRepository.save(newBookmark);
                        }
                );
    }
}
