package com.example.demo.mypage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NicknameUpdateRequest {
    private String nickname;
    private Boolean isChecked;
}
