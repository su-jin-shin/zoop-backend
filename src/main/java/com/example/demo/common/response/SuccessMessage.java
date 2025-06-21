package com.example.demo.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    GET_NOTIFICATION_SUCCESS("알림 목록 조회에 성공했습니다."),
    GET_SUCCESS("요청이 정상적으로 처리되었습니다."),
    CREATED_SUCCESSFULLY("성공적으로 등록되었습니다."),
    UPDATE_NICKNAME_SUCCESS("닉네임이 정상적으로 수정되었습니다."),
    PROFILE_IMAGE_UPDATED("프로필 이미지가 수정되었습니다."),
    PROFILE_IMAGE_RESET("기본 프로필 이미지로 변경되었습니다."),
    GET_ACCOUNT_INFO_SUCCESS("내 정보 조회에 성공했습니다."),

    REVIEW_CREATED("리뷰 등록에 성공했습니다."),
    REVIEW_FETCHED("리뷰 조회에 성공했습니다."),
    REVIEW_UPDATED("리뷰 수정에 성공했습니다."),
    REVIEW_DELETED("리뷰 삭제에 성공했습니다."),
    REVIEW_LIKED("리뷰 좋아요 상태가 변경되었습니다."),
    REVIEW_LIKE_STATUS_FETCHED("리뷰 좋아요 상태 조회에 성공했습니다."),
    REVIEW_LIKE_COUNT_FETCHED("리뷰 좋아요 개수 조회에 성공했습니다."),
    COMMENT_COUNT_FETCHED("댓글 개수 조회에 성공했습니다."),

    GET_COMMENT_LIST_SUCCESS("댓글 목록 조회에 성공했습니다."),
    COMMENT_CREATED("댓글이 성공적으로 등록되었습니다."),
    COMMENT_UPDATED("댓글이 성공적으로 수정되었습니다."),
    COMMENT_DELETED("댓글이 성공적으로 삭제되었습니다."),
    COMMENT_LIKE_STATUS_UPDATED("댓글 좋아요 상태가 업데이트되었습니다."),
    GET_COMMENT_COUNT("댓글 개수 조회에 성공했습니다."),
    GET_COMMENT_LIKE_COUNT("댓글 좋아요 개수 조회에 성공했습니다."),
    GET_COMMENT_LIKE_STATUS("댓글 좋아요 여부 조회에 성공했습니다."),

    BOOKMARKED_PROPERTIES_FETCHED("찜한 매물 목록 조회에 성공했습니다."),
    BOOKMARKED_PROPERTIES_EXPORTED("찜한 매물 엑셀 다운로드가 완료되었습니다."),
    BOOKMARKED_PROPERTIES_FOR_MAP_FETCHED("찜한 매물 지도 정보 조회에 성공했습니다."); // 성공


    private final String message;
}
