package com.example.demo.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImageUpdateRequest {
    @NotBlank
    private String profileImageUrl;
}
