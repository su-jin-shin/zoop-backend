package com.example.demo.mypage.dto;

// 이미지 조회
public class ProfileImageResponse {
    private String profileImageUrl;

    public ProfileImageResponse(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}