package com.example.demo.mypage.service.impl;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import com.example.demo.mypage.service.ProfileImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final MypageUserInfoRepository userInfoRepository;

    // 시스템에서 사용할 기본 프로필 이미지 URL
    private static final String DEFAULT_PROFILE_IMAGE_URL = "http://localhost:8080/images/default-profile.png";

    @Override
    @Transactional
    public String updateProfileImage(Long userId, String profileImageUrl) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        user.setProfileImage(profileImageUrl);  // setter 사용
        return user.getProfileImage();          // 저장된 값 반환
    }

    @Override
    @Transactional
    public String resetToDefaultImage(Long userId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        user.setProfileImage(DEFAULT_PROFILE_IMAGE_URL);
        return user.getProfileImage();  // 기본 이미지 URL 반환
    }
}
