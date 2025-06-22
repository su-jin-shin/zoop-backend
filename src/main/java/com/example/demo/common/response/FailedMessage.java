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
    RESOURCE_NOT_FOUND("요청하신 정보를 찾을 수 없습니다."),

    DUPLICATED_NICKNAME("이미 사용 중인 닉네임입니다."),
    CHECK_AVAILABLE_NICKNAME("사용 가능한 닉네임 입니다."),
    DUPLICATED_KEYWORD_FILTER_HISTORY("이미 등록된 필터 조건 입니다."),
    FILE_NOT_FOUND("이미지 파일이 없습니다."),
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다."),

    PROPERTY_NOT_FOUND("매물을 찾을 수 없습니다."),
    INVALID_REVIEW_CONTENT("리뷰 내용이 유효하지 않습니다."),
    LOGIN_REQUIRED("로그인이 필요합니다."),
    INVALID_IS_LIKED("isLiked 값이 필요합니다."),
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),

    EXCEL_EXPORT_FAILED("액셀 생성 실패");

    private final String message;
}
