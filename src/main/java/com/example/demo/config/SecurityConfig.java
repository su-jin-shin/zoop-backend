package com.example.demo.config;

import com.example.demo.config.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;          // (1) 필터 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                /* --- CSRF --- */
                .csrf(csrf -> csrf.disable())

                /* --- 세션 전략 : 필요 시 생성 --- */
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                /* --- 경로별 권한 --- */
                .authorizeHttpRequests(auth -> auth
                        /* Swagger & 정적 리소스 ― 모두 허용 */
                        .requestMatchers(
                                "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico"
                        ).permitAll()
                        /* 소셜 로그인 엔드포인트 ― 모두 허용 */
                        .requestMatchers("/users/auth/**").permitAll()
                        /* 이외 모든 요청 ― 인증 필요 */
                        .anyRequest().authenticated())

                /* --- JWT 필터 등록 (UsernamePasswordAuthenticationFilter 앞) --- */
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
