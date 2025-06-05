package com.example.demo.auth.controller;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.service.KakaoAuthService;
import com.example.demo.auth.service.LoginService;
import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class AuthController {


    private final LoginService loginService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final UserInfoRepository userRepo;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 카카오 인가 코드 받으러 이동
    @GetMapping("/kakao/login")
    public ResponseEntity<Void> redirectToKakao(
            @RequestParam(value = "state", required = false) String state) {

        String url = UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "account_email,profile_image")
                .queryParamIfPresent("state", Optional.ofNullable(state))
                .build()
                .encode()
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    // 카카오가 redirect_uri로 돌려준 code 처리 + 로그인 기록(IP)까지!
    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(
            @RequestParam String code,
            HttpServletRequest request) {

        String clientIp = getClientIpAddress(request);
        LoginResponseDto res = loginService.kakaoLogin(code, clientIp);

        String redirect;
        if (res.getNeedsNickname()) {
            redirect = UriComponentsBuilder.fromUriString("/nickname.html")
                    .queryParam("email", jwtUtil.getSubject(res.getAccessToken()))
                    .build()
                    .encode()
                    .toUriString();
        } else {
            redirect = UriComponentsBuilder.fromUriString("/home.html")
                    .queryParam("accessToken", res.getAccessToken())
                    .queryParam("refreshToken", res.getRefreshToken())
                    .queryParam("kakaoToken", res.getKakaoAccessToken())
                    .queryParam("nickname", res.getNickname())
                    .build()
                    .encode()
                    .toUriString();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirect)
                .build();
    }

    // 신규 사용자 닉네임 등록
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody Map<String,String> body) {
        loginService.registerNickname(body.get("email"), body.get("nickname"));
        return ResponseEntity.ok().build();
    }

    // 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody Map<String,String> body) {

        String refresh = body.get("refreshToken");
        if (refresh == null || jwtUtil.isExpired(refresh))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = jwtUtil.getSubject(refresh);
        String newAccess = jwtUtil.generateAccess(email);
        String nickname = userRepo.findByEmail(email)
                .map(UserInfo::getNickname)
                .orElse(null);

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .accessToken(newAccess)
                        .refreshToken(refresh)
                        .needsNickname(false)
                        .nickname(nickname)
                        .build()
        );
    }

    // 로그아웃 – 카카오 API 실패해도 204 반환
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Kakao-Access") String kakaoAccess) {

        try {
            kakaoAuthService.logout(kakaoAccess);
        } catch (Exception e) {
            log.warn("Kakao logout failed but local logout continues: {}", e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    // 클라이언트 IP주소 추출 (헤더 → 없으면 RemoteAddr)
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
