package com.example.demo.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NicknameUpdateRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 20, message = "닉네임을 1~20자 범위 내로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 특수문자와 공백을 포함할 수 없습니다.")
    private String nickname;

}
