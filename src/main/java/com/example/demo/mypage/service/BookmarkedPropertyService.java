package com.example.demo.mypage.service;

import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.property.dto.PropertyListItemDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkedPropertyService {

    MyPropertyPageResponse getBookmarkedProperties(Long userId, Pageable pageable); // 마이페이지용
//    BookmarkedPropertyPageResponse getBookmarkedProperties(Long userId, int page, int size, String sort); //지도용

    // 지도용 전체 리스트
    List<MapPropertyDto> getMapProperties(Long userId);

    // 바텀시트용 정렬 + 페이지
    MyPropertyPageResponse getPagedProperties(Long userId, int page, int size, String sort);

    List<PropertyListItemDto> getAllBookmarkedPropertyResponses(Long userId);

    List<PropertyExcelDto> getBookmarkedPropertiesForExcel(Long userId);

}
