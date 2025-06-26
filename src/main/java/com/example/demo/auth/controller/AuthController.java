package com.example.demo.auth.controller;

import com.example.demo.auth.annotation.CheckUserOwner;
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
// AuthController
    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam String code,
                                              HttpServletRequest request) {

        String clientIp = getClientIpAddress(request);
        LoginResponseDto res = loginService.kakaoLogin(code, clientIp, request);

        /* --- ① 토큰을 HttpOnly 쿠키로 설정 --- */
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", res.getAccessToken())
                .httpOnly(false)
                .secure(true)             // https니까 그대로 유지
                .domain("zoopzoop.shop")
                .sameSite("None")         // cross-site 대응 필수
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", res.getRefreshToken())
                .httpOnly(false)
                .secure(true)
                .domain("zoopzoop.shop")
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(14))           // 원하는 기간
                .build();

        ResponseCookie kakaoCookie = ResponseCookie.from("KAKAO_ACCESS", res.getKakaoAccessToken())
                .httpOnly(false)
                .secure(true)
                .domain("zoopzoop.shop")
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        /* --- ② URL 프래그먼트에 토큰을 실어 보낼 필요가 없으므로 제거 --- */
        String redirect = front.buildRedirect(res.getNeedsNickname());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirect)
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, kakaoCookie.toString())
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
            /* ③ 헤더 대신 쿠키에서 꺼낸다 */
            @CookieValue("REFRESH_TOKEN") String refresh) {

        if (jwtUtil.isExpired(refresh))
            throw new UnauthorizedAccessException();

        String email     = jwtUtil.getSubject(refresh);
        String newAccess = jwtUtil.generateAccess(email);

        // 새 access 토큰을 다시 쿠키로 내려줌
        ResponseCookie newAccessCookie = ResponseCookie.from("ACCESS_TOKEN", newAccess)
                .httpOnly(false).secure(true).sameSite("None")
                .domain("zoopzoop.shop")
                .path("/").maxAge(Duration.ofMinutes(15)).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                .body(Map.of("result", "ok"));      // 바디는 아무거나
    }

    /* -------------------------------------------------
     * 5) 로그아웃
     * ------------------------------------------------- */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("KAKAO_ACCESS") String kakaoAccess) {
        try { kakaoAuthService.logout(kakaoAccess); } catch(Exception ignored){ }

        // ④ maxAge=0 쿠키로 덮어써서 삭제
        ResponseCookie del = ResponseCookie.from("ACCESS_TOKEN", "")
                .domain("zoopzoop.shop")
                .path("/").maxAge(0).build();

        ResponseCookie delR = ResponseCookie.from("REFRESH_TOKEN", "")
                .domain("zoopzoop.shop")
                .path("/").maxAge(0).build();

        ResponseCookie delK = ResponseCookie.from("KAKAO_ACCESS", "")
                .domain("zoopzoop.shop")
                .path("/").maxAge(0).build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, del.toString())
                .header(HttpHeaders.SET_COOKIE, delR.toString())
                .header(HttpHeaders.SET_COOKIE, delK.toString())
                .build();
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
