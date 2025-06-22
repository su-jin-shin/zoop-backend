package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.excel.ExcelGenerator;
import com.example.demo.common.excel.ExcelListResponse;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.excel.PropertyExcelMetaProvider;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.PropertyMapResponse;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.service.BookmarkedPropertyService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/histories/bookmarked-properties")
public class BookmarkedPropertyController {

    private final BookmarkedPropertyService bookmarkedPropertyService;
    private final PropertyExcelMetaProvider propertyExcelMetaProvider;
    private final ExcelGenerator excelGenerator;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult> getBookmarkedPropertiesByPageable(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = parseUserId(loginUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        MyPropertyPageResponse result = bookmarkedPropertyService.getBookmarkedProperties(userId, pageable);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.BOOKMARKED_PROPERTIES_FETCHED.getMessage(),
                        result
                )
        );
    }
    @GetMapping("/map")
    public ResponseEntity<ExcelListResponse<PropertyExcelDto>> getRecentViewedPropertiesForMap(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = parseUserId(loginUser);

        List<PropertyExcelDto> dtoList = bookmarkedPropertyService.getBookmarkedPropertiesForExcel(userId);
        ExcelListResponse<PropertyExcelDto> response = ExcelListResponse.<PropertyExcelDto>builder()
                .countProperties(dtoList.size())
                .data(dtoList)
                .build();

        return ResponseEntity.ok(response); // ✅ 지도 + 엑셀 둘 다 활용 가능
    }

}
