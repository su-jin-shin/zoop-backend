package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.MyPageAccountResponse;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageAccountService {

    private final MypageUserInfoRepository userInfoRepository;
    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
    }

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public MyPageAccountResponse getAccountInfo(Long userId) {
        validateUserId(userId);

        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String imageUrl = user.getProfileImage();
        if (imageUrl == null || imageUrl.isBlank()) {
            imageUrl = "/images/default-profile.png";
        }
        if (!imageUrl.startsWith("http")) {
            imageUrl = baseUrl + imageUrl;
        }

        return MyPageAccountResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(imageUrl)
                .build();
    }
}
