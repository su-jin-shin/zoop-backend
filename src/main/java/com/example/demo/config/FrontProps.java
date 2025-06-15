package com.example.demo.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "front")
@Getter
@RequiredArgsConstructor
public class FrontProps {
    private final String baseUrl;       // "" or "https://app.foo.com"
    private final String nicknamePath;  // "/nickname.html" or "/onboarding/nickname"
    private final String homePath;      // "/home.html" or "/dashboard"

    /** 최종 redirect URL 생성 */
    public String buildRedirect(boolean needsNickname) {
        String path = needsNickname ? nicknamePath : homePath;
        return (baseUrl == null || baseUrl.isBlank()) ? path   // 상대경로
                : baseUrl + path;
    }
}