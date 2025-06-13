package com.example.demo.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    GET_NOTIFICATION_SUCCESS("알림 목록 조회에 성공했습니다."),
    GET_SUCCESS("요청이 정상적으로 처리되었습니다."),

    UPDATE_NICKNAME_SUCCESS("닉네임이 정상적으로 수정되었습니다."),
    PROFILE_IMAGE_UPDATED("프로필 이미지가 수정되었습니다."),
    PROFILE_IMAGE_RESET("기본 프로필 이미지로 변경되었습니다."),
    GET_ACCOUNT_INFO_SUCCESS("내 정보 조회에 성공했습니다.");

    private final String message;
}
