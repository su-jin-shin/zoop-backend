package com.example.demo.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameCheckResponse {

    private boolean isDuplicated;

    @JsonProperty("isDuplicated")
    public boolean getIsDuplicated() {
        return isDuplicated;
    }
}
