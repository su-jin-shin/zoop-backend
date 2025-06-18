package com.example.demo.auth.factory;

import com.example.demo.auth.domain.UserInfo;

public class UserInfoFactory {
    public static UserInfo createFromKakao(Long kakaoId, String email, String profileImage) {
        UserInfo user = new UserInfo();
        user.setKakaoId(kakaoId);
        user.setEmail(email);
        user.setProfileImage(profileImage);
        return user;
    }
}