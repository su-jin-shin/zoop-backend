package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {


    // 전체허용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 모든 요청 허가
                );

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api-docs",
//                                "/api-docs/**",    // prefix match
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/users/**",
//                                "/login.html",
//                                "/css/**",      // CSS 경로 (예: /css/style.css)
//                                "/js/**",       // JS 경로 (예: /js/app.js)
//                                "/images/**",   // 이미지 경로 (예: /images/logo.png)
//                                "/favicon.ico"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                );




        return http.build();
    }
}
