package com.example.demo.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FailedMessage {

    INPUT_NOT_VALID("입력값이 잘못되었습니다!"),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");
    private final String message;
}
