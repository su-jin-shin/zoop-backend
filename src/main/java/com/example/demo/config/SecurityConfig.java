package com.example.demo.config;



import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

//@Configuration
//@RequiredArgsConstructor
//@SuppressFBWarnings(value = "EI_EXPOSE_REP2",
//        justification = "JwtAuthFilter는 Spring DI로 주입되며, 외부 변경 위험이 없습니다.")
//public class SecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(sm ->
//                        sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//
//                .authorizeHttpRequests(auth -> auth
//                        // 헬스 체크·Swagger·회원가입 등 “익명 허용” 경로
//                        .requestMatchers("/hc").permitAll()
//                        .requestMatchers("/mypage/check-user-nickname").permitAll()
//                        .requestMatchers(
//                                "/api-docs/**", "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/users/auth/kakao/login",
//                                "/users/auth/kakao/callback",
//                                "/users/auth/refresh",
//                                "/users/auth/logout",
//                                "/css/**", "/js/**", "/images/**", "/favicon.ico"
//                        ).permitAll()
//                        // 그 외는 모두 인증 필요
//                        .anyRequest().authenticated()
//                )
//
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}

@Configuration
@RequiredArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "JwtAuthFilter는 Spring DI로 주입되며, 외부 변경 위험이 없습니다.")
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())                 // 프론트 호출 허용
                .csrf(csrf -> csrf.disable())                   // CSRF 끔 (쿠키 기반 API에서 필요 없음)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비사용 (JWT 기반)
                .authorizeHttpRequests(auth -> auth
                        // 로그인 인증 없이 허용할 API만 열기
                        .requestMatchers(
                                "/users/auth/kakao/login",
                                "/users/auth/kakao/callback",
                                "/users/auth/refresh",
                                "/users/auth/logout",
                                "/hc"
                        ).permitAll()
                        // 그 외는 모두 인증 필요 (LoginUser 주입 대상)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
