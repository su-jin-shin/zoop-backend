package com.example.demo.config;

import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwt;
    private final UserDetailsService userDetailsService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        // 1️⃣ 요청 URI·Origin 찍기 (선택)
        log.debug("[JWT] {} {}", req.getMethod(), req.getRequestURI());

        // 2️⃣ ACCESS_TOKEN 쿠키 확인
        Cookie accessCookie = WebUtils.getCookie(req, "ACCESS_TOKEN");
        if (accessCookie == null) {
            log.debug("[JWT] ACCESS_TOKEN 쿠키가 없습니다");
        } else {
            String access = accessCookie.getValue();
            log.debug("[JWT] ACCESS_TOKEN={}", access.substring(0, 20) + "...");

            // 3️⃣ 만료 여부
            if (jwt.isExpired(access)) {
                log.debug("[JWT] 토큰 만료 → 인증 처리 안 함");
            } else {
                try {
                    String email = jwt.getSubject(access);
                    log.debug("[JWT] subject(email)={}", email);

                    var user = userDetailsService.loadUserByUsername(email);
                    var auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("[JWT] SecurityContext 인증 완료");
                } catch (Exception e) {
                    log.warn("[JWT] 토큰 검증 실패: {}", e.getMessage());
                }
            }
        }

        chain.doFilter(req, res);
    }
}
