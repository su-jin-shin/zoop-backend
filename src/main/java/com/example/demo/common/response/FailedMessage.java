package com.example.demo.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FailedMessage {

    INPUT_NOT_VALID("입력값이 잘못되었습니다!"),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),

    USER_NOT_FOUND("로그인 후 다시 시도해주세요."),
    UNAUTHORIZED_ACCESS("접근 권한이 없습니다."),
    BAD_REQUEST_DEFAULT("잘못된 요청입니다."),
    RESOURCE_NOT_FOUND("요청하신 정보를 찾을 수 없습니다.");
    private final String message;
}
