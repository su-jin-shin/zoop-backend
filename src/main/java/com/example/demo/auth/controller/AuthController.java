package com.example.demo.auth.controller;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.service.KakaoAuthService;
import com.example.demo.auth.service.LoginService;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.config.FrontProps;
import com.example.demo.mypage.service.NicknameService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService         loginService;
    private final KakaoAuthService     kakaoAuthService;
    private final JwtUtil              jwtUtil;
    private final NicknameService      nicknameService;
    private final UserInfoRepository   userInfoRepository;
    private final FrontProps front;

    @Value("${kakao.client-id}")    private String kakaoClientId;
    @Value("${kakao.redirect-uri}") private String redirectUri;

    /* -------------------------------------------------
     * 1) 카카오 인가 코드 받으러 이동
     * ------------------------------------------------- */
    @GetMapping("/kakao/login")
    public ResponseEntity<Void> redirectToKakao(@RequestParam(required = false) String state) {

        String url = UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id",     kakaoClientId)
                .queryParam("redirect_uri",  redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope",         "account_email,profile_image")
                .queryParamIfPresent("state", Optional.ofNullable(state))
                .build()
                .encode()
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    /* -------------------------------------------------
     * 2) 카카오 콜백 처리
     * ------------------------------------------------- */
    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam String code,
                                              HttpServletRequest request) {

        String clientIp = getClientIpAddress(request);
        LoginResponseDto res = loginService.kakaoLogin(code, clientIp, request);

        /* --- 쿠키 삭제 → URL fragment 전달 --- */
        String redirect = front.buildRedirect(res.getNeedsNickname()) +
                "#access_token="  + res.getAccessToken() +
                "&refresh_token=" + res.getRefreshToken() +
                "&kakao_access="  + res.getKakaoAccessToken() +
                "&needsNickname=" + res.getNeedsNickname();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirect)
                .build();
    }
    /* -------------------------------------------------
     * 3) 신규 사용자 닉네임 등록
     * ------------------------------------------------- */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@AuthenticationPrincipal LoginUser loginUser,
                                         @RequestBody Map<String, String> body) {

        String nickname = body.get("nickname");
        if (nickname == null || nickname.isBlank()) {
            throw new InvalidRequestException();   // 400
        }

        Long userId = Long.valueOf(loginUser.getUsername());
        nicknameService.updateNickname(userId, nickname);

        return ResponseEntity.ok().build();        // 200
    }

    /* -------------------------------------------------
     * 4) 액세스 토큰 재발급
     * ------------------------------------------------- */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedAccessException();

        String refresh = authHeader.substring(7);           // "Bearer " 제거
        if (jwtUtil.isExpired(refresh))
            throw new UnauthorizedAccessException();

        String email       = jwtUtil.getSubject(refresh);
        String newAccess   = jwtUtil.generateAccess(email);

        return ResponseEntity.ok(Map.of("access_token", newAccess));
    }
    /* -------------------------------------------------
     * 5) 로그아웃
     * ------------------------------------------------- */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Kakao-Access") String kakaoAccess) {
        try {
            kakaoAuthService.logout(kakaoAccess);
        } catch (Exception e) {
            log.warn("Kakao logout failed (ignored): {}", e.getMessage());
        }
        return ResponseEntity.noContent().build(); // 204
    }

    /* -------------------------------------------------
     * 6) 로그인된 사용자 정보 조회
     * ------------------------------------------------- */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UnauthorizedAccessException::new);

        Map<String, Object> dto = Map.of(
                "userId",       user.getUserId(),
                "email",        user.getEmail(),
                "nickname",     user.getNickname(),
                "profileImage", user.getProfileImage()
        );
        return ResponseEntity.ok(dto);
    }

    /* ---------------------------------- ---------------
     * UTIL : 클라이언트 IP 추출
     * ------------------------------------------------- */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}
