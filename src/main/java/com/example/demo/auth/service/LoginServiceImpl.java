package com.example.demo.auth.service;

import com.example.demo.auth.domain.LoginHistory;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.KakaoTokenResponse;
import com.example.demo.auth.dto.KakaoUserDto;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.factory.LoginHistoryFactory;
import com.example.demo.auth.factory.UserInfoFactory;
import com.example.demo.auth.repository.LoginHistoryRepository;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final KakaoAuthService        kakaoAuthService;
    private final UserInfoRepository      userRepo;
    private final LoginHistoryRepository  loginHistoryRepo;
    private final JwtUtil                 jwtUtil;

    /* -------------------------------------------------
     * 카카오 로그인
     * ------------------------------------------------- */
    @Transactional
    @Override
    public LoginResponseDto kakaoLogin(String code,
                                       String clientIp,
                                       HttpServletRequest request) {

        /* 1) 카카오 토큰 · 프로필 조회 */
        KakaoTokenResponse tokenRes = kakaoAuthService.getToken(code);
        KakaoUserDto       userDto  = kakaoAuthService.getUser(tokenRes.getAccessToken());

        String email   = userDto.getKakaoAccount().getEmail();
        String profile = userDto.getKakaoAccount().getProfile().getProfileImageUrl();
        Long   kakaoId = userDto.getId();

        /* 2) “활성” 계정 조회 (deletedAt IS NULL) */
        UserInfo user = userRepo.findByEmailAndDeletedAtIsNull(email).orElse(null);

        /* 3) 없으면 새로 생성 */
        if (user == null) {
            user = UserInfoFactory.createFromKakao(kakaoId, email, profile);
            userRepo.save(user);
        } else {
            // 활성 계정이라면 최신 프로필만 갱신
            if (user.getProfileImage() == null) {
                user.setProfileImage(profile);
            }
            user.setLastLoginAt(LocalDateTime.now());
        }

        /* 4) 로그인 히스토리 기록 */
        LoginHistory history = LoginHistoryFactory.create(user, clientIp);
        loginHistoryRepo.save(history);

        /* 5) Spring Security 인증 컨텍스트 수립 */
        LoginUser principal = new LoginUser(user);
        var authToken = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);            // JSESSIONID 발급
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        /* 6) JWT 발급 */
        String access  = jwtUtil.generateAccess(email);
        String refresh = jwtUtil.generateRefresh(email);
        boolean needsNickname =
                (user.getNickname() == null || user.getNickname().isBlank());

        /* 7) 응답 DTO */
        return LoginResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .kakaoAccessToken(tokenRes.getAccessToken())
                .needsNickname(needsNickname)
                .nickname(user.getNickname())
                .build();
    }
}
