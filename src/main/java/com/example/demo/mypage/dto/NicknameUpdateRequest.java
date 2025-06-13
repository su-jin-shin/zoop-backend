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
    @Size(min = 2, max = 10, message = "닉네임을 2~10자 범위 내로 입력해주세요.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]+$",
            message = "닉네임은 한글, 영문 대소문자, 숫자만 사용할 수 있으며 공백과 특수문자는 사용할 수 없습니다."
    )
    private String nickname;

}
