package com.example.demo.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "front")
public class FrontProps {
    private final String baseUrl;
    private final String nicknamePath;
    private final String homePath;

    public FrontProps(String baseUrl, String nicknamePath, String homePath) {
        this.baseUrl = baseUrl;
        this.nicknamePath = nicknamePath;
        this.homePath = homePath;
    }

    public String buildRedirect(boolean needsNickname) {
        String path = needsNickname ? nicknamePath : homePath;
        return (baseUrl == null || baseUrl.isBlank()) ? path : baseUrl + path;
    }
}
