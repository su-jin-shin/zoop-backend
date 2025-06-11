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

        String token = resolveAccessToken(req); // 🔑 쿠키에서 JWT 추출
        if (token != null && !jwtUtil.isExpired(token)) {
            String email = jwtUtil.getSubject(token);

            userRepo.findByEmail(email).ifPresent(userInfo -> {
                LoginUser principal = new LoginUser(userInfo);
                var auth = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug(" JWT 인증 완료 - userId: {}", userInfo.getUserId());
            });
        }

        // 토큰이 없거나 만료된 경우에도 인증 없이 계속 진행
        chain.doFilter(req, res);
    }

    private String resolveAccessToken(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
