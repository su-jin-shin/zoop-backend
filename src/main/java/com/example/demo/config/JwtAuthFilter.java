package com.example.demo.config;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserInfoRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String token = resolveAccessToken(req);          // ① 쿠키(우선) 또는 헤더에서 토큰 추출
        if (token != null) {
            try {
                if (!jwtUtil.isExpired(token)) {
                    String email = jwtUtil.getSubject(token);

                    userRepo.findByEmail(email).ifPresent(userInfo -> {
                        LoginUser principal = new LoginUser(userInfo);
                        var auth = new UsernamePasswordAuthenticationToken(
                                principal, null, principal.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.debug("✅ JWT 인증 성공 - userId: {}", userInfo.getUserId());
                    });
                }
            } catch (Exception e) {          // 서명 불일치·포맷 오류 등
                log.warn("❌ JWT 검증 실패: {}", e.getMessage());
            }
        }

        // 인증 없이도 접근 가능한 경로가 있으므로, 체인은 항상 진행
        chain.doFilter(req, res);
    }

    /**
     * access_token 쿠키를 우선 사용하고, 없으면 Authorization 헤더(Bearer)를 fallback 으로 사용
     */
    private String resolveAccessToken(HttpServletRequest req) {

        // ── 1) HttpOnly 쿠키
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // ── 2) Authorization: Bearer <token>  (테스트 툴·모바일 앱 등을 위한 백업 경로)
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
