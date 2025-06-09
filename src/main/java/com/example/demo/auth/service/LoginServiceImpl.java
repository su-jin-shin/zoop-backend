package com.example.demo.auth.service;

import com.example.demo.auth.domain.LoginHistory;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.KakaoTokenResponse;
import com.example.demo.auth.dto.KakaoUserDto;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.repository.LoginHistoryRepository;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final KakaoAuthService kakaoAuthService;
    private final UserInfoRepository userRepo;
    private final LoginHistoryRepository loginHistoryRepo;
    private final JwtUtil jwtUtil;

    // 로그인 처리 (+ 로그인 히스토리 저장)
    @Transactional
    @Override
    public LoginResponseDto kakaoLogin(String code, String ipAddress) {

        KakaoTokenResponse tokenRes = kakaoAuthService.getToken(code);
        KakaoUserDto userDto = kakaoAuthService.getUser(tokenRes.getAccessToken());

        String email = userDto.getKakaoAccount().getEmail();
        String profile = userDto.getKakaoAccount().getProfile().getProfileImageUrl();
        Long kakaoId = userDto.getId();

        UserInfo user;
        Boolean exists = userRepo.existsByEmail(email);
        if (!exists) {
            user = new UserInfo();
            user.setKakaoId(kakaoId);
            user.setEmail(email);
            user.setProfileImage(profile);
            userRepo.save(user);
        } else {
            user = userRepo.findByEmail(email).orElseThrow();
            user.setLastLoginAt(LocalDateTime.now());
            userRepo.save(user);
        }

        // 로그인 히스토리 저장
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setLoginAt(LocalDateTime.now());
        history.setIpAddress(ipAddress);
        loginHistoryRepo.save(history);

        String access = jwtUtil.generateAccess(email);
        String refresh = jwtUtil.generateRefresh(email);
        String nickname = user.getNickname();

        return LoginResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .kakaoAccessToken(tokenRes.getAccessToken())
                .needsNickname(!exists)
                .nickname(nickname)
                .build();
    }

    @Override
    public void registerNickname(String email, String nickname) {
        userRepo.findByEmail(email).ifPresent(u -> {
            u.setNickname(nickname);
            userRepo.save(u);
        });
    }
}
