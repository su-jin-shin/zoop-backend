package com.example.demo.auth.service;

import com.example.demo.auth.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

public interface LoginService {


    void registerNickname(String email, String nickname);

    LoginResponseDto kakaoLogin(String code,
                                       String clientIp,
                                       HttpServletRequest request);

}
