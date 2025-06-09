package com.example.demo.mypage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageAccountResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;
}
