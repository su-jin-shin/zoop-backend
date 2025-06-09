package com.example.demo.auth.service;

import com.example.demo.auth.dto.KakaoTokenResponse;
import com.example.demo.auth.dto.KakaoUserDto;

public interface KakaoAuthService {

    KakaoTokenResponse getToken(String code);

    KakaoUserDto getUser(String accessToken);

    void logout(String kakaoAccessToken);
}
