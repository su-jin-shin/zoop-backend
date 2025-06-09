package com.example.demo.auth.dto;

import lombok.Builder;
import lombok.Getter;


@Builder @Getter
public class LoginResponseDto {
    private String accessToken;       // 우리 JWT
    private String refreshToken;      // 우리 JWT
    private String kakaoAccessToken;  // 카카오 REST access_token
    private Boolean needsNickname;
    private String nickname;
}