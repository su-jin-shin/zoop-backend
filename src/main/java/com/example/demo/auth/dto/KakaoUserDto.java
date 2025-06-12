package com.example.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "외부 API 응답 DTO 그대로 직렬화/역직렬화")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDto {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    /* ------------- 내부 클래스 ------------- */
    @Data
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
    public static class KakaoAccount {
        private String email;

        @JsonProperty("profile")
        private Profile profile;

        @Data
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
        public static class Profile {
            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}
