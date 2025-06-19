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

    private final KakaoAuthService kakaoAuthService;
    private final UserInfoRepository userRepo;
    private final LoginHistoryRepository loginHistoryRepo;
    private final JwtUtil jwtUtil;

    /* -------------------------------------------------
     * 카카오 로그인
     * ------------------------------------------------- */
    @Transactional
    @Override
    public LoginResponseDto kakaoLogin(String code,
                                       String clientIp,
                                       HttpServletRequest request) {

        /* 1) 토큰·카카오 유저 조회 */
        KakaoTokenResponse tokenRes = kakaoAuthService.getToken(code);
        KakaoUserDto userDto       = kakaoAuthService.getUser(tokenRes.getAccessToken());

        String email   = userDto.getKakaoAccount().getEmail();
        String profile = userDto.getKakaoAccount().getProfile().getProfileImageUrl();
        Long   kakaoId = userDto.getId();

        /* 2) 탈퇴 회원 복구 또는 신규 생성 */
        UserInfo user = userRepo.findByEmail(email).orElse(null);



            if (user != null) {
                user.reactivate();          // ← 탈퇴 상태면 deletedAt·withdrawReason NULL 처리
            } else {
                user = UserInfoFactory.createFromKakao(kakaoId, email, profile);
            }


        user.setLastLoginAt(LocalDateTime.now());
        user.setProfileImage(profile);      // 프로필 이미지 최신화
        userRepo.save(user);


        // 3) 로그인 히스토리 기록
        LoginHistory history = LoginHistoryFactory.create(user, clientIp);
        loginHistoryRepo.save(history);

        // 4) SecurityContext 생성 & 세션 저장
        LoginUser principal = new LoginUser(user);
        var authToken = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true); // JSESSIONID 생성/획득
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        /* 5) JWT 발급 */
        String access  = jwtUtil.generateAccess(email);
        String refresh = jwtUtil.generateRefresh(email);
        boolean needsNickname = (user.getNickname() == null || user.getNickname().isBlank());

        return LoginResponseDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .kakaoAccessToken(tokenRes.getAccessToken())
                .needsNickname(needsNickname)
                .nickname(user.getNickname())
                .build();
    }


}
