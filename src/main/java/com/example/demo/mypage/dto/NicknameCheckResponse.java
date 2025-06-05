package com.example.demo.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameCheckResponse {
    private boolean isDuplicated;
    private String message;
}
