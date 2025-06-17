package com.example.demo.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyMapResponse {

    // 지도용: 모든 매물 요약 (좌표 + ID)
    private List<MapPropertyDto> mapProperties;

    // 바텀시트용: 페이징 목록
    private BookmarkedPropertyPageResponse bottomSheet;
}
