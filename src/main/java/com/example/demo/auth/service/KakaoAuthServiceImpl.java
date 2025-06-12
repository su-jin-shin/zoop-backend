package com.example.demo.auth.service;

import com.example.demo.auth.dto.KakaoTokenResponse;
import com.example.demo.auth.dto.KakaoUserDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"},
        justification = "WebClient는 Bean이며 불변 객체로 안전하게 주입됨")
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final WebClient kauthClient;
    private final WebClient kapiClient;

    @Value("${kakao.client-id}")    private String clientId;
    @Value("${kakao.redirect-uri}") private String redirectUri;

    @Autowired
    public KakaoAuthServiceImpl(
            @Qualifier("kakaoAuthWebClient") WebClient kauthClient,
            @Qualifier("kakaoApiWebClient")  WebClient kapiClient) {
        this.kauthClient = kauthClient;
        this.kapiClient  = kapiClient;
    }

    @Override
    public KakaoTokenResponse getToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return kauthClient.post()
                .uri("/oauth/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    @Override
    public KakaoUserDto getUser(String kakaoAccessToken) {
        return kapiClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoUserDto.class)
                .block();
    }

    @Override
    public void logout(String kakaoAccessToken) {
        kapiClient.post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.warn("Kakao logout failed (ignored): {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
    }
}
