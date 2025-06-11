package com.example.demo.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    GET_NOTIFICATION_SUCCESS("알림 목록 조회에 성공했습니다.");

    private final String message;
}
