package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequestDto {

    @NotBlank(message = "탈퇴 사유는 필수입니다.")
    private String withdrawReason;
}
