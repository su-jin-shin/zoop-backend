package com.example.demo.mypage.service;

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

    @Override
    @Transactional
    public String updateProfileImage(Long userId, String profileImageUrl) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        user.setProfileImage(profileImageUrl);  // setter 사용
        return user.getProfileImage();          // 저장된 값 반환
    }

}
