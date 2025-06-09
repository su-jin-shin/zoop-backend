package com.example.demo.config;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserInfoRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String token = resolveAccessToken(req);          // ① 토큰 추출
        if (token != null && !jwtUtil.isExpired(token)) {
            String email = jwtUtil.getSubject(token);    // ② 이메일 꺼내기

            userRepo.findByEmail(email).ifPresent(userInfo -> {
                LoginUser principal = new LoginUser(userInfo);   // ③ LoginUser 래핑
                var auth = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth); // ④ 세팅
            });
        }
        chain.doFilter(req, res);                        // ⑤ 필터 체인 진행
    }

    private String resolveAccessToken(HttpServletRequest req) {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            return h.substring(7);
        }
        return null;
    }
}