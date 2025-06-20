package com.example.demo.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPageUserDto {
    private String nickname;

    public MyPageUserDto(MyPageUserDto other) {
        this.nickname = other.nickname;
    }
}
