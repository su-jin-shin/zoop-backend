package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // kauth.kakao.com : 토큰 발급 · 갱신
    @Bean
    public WebClient kakaoAuthWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    // kapi.kakao.com : 사용자 API, 로그아웃 등
    @Bean
    public WebClient kakaoApiWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();                      // GET 요청엔 Content-Type 불필요
    }
}
