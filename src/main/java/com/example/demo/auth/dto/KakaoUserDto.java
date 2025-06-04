package com.example.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class KakaoUserDto {
    private Long id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        private String email;
        @JsonProperty("profile") private Profile profile;
        @Data public static class Profile {
            @JsonProperty("profile_image_url") private String profileImageUrl;
        }
    }
}