package com.example.demo.Filter.service;

import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.chat.dto.ChatRoomRequestDto;
//import com.example.demo.Filter.dto.response.KeywordFilterHistoryResponseDto;

import java.util.List;

public interface FilterService {

    // 알림을 통해서 저장되는 필터
    void saveKeywordFilter(Long userId, FilterRequestDto filterRequestDto);

    // 채팅을 통해서 저장되는 필터
    void saveChatFilter(FilterRequestDto filterRequestDto, Long chatRoomId);

    // 사용자가 기존의 필터 조건을 다른 필터 조건으로 수정
    void modifyKeywordFilter(Long userId, Long keywordFilterHistoryId, FilterRequestDto updateRequestDto);

    // 기존 키워드 필터 히스트로리에서 삭제 ( 실제로는 isUsed = true -> false 처리)
    void deactivateKeywordFilter(Long userId, Long keywordFilterHistoryId);

    // 사용자 등록한 필터 조건 전체 목록 조회
    List<String> getAllFilterTitlesByUser(Long userId);

    // 사용자 등록한 필터 조건 상세 조회
    //KeywordFilterHistoryResponseDto getKeywordFilterDetail(Long userId, Long keywordFilterHistoryId);


}
