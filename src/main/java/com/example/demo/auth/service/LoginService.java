package com.example.demo.auth.service;

import com.example.demo.auth.dto.LoginResponseDto;
import jakarta.transaction.Transactional;

public interface LoginService {

    LoginResponseDto kakaoLogin(String code, String ipAddress);

    void registerNickname(String email, String nickname);

}
