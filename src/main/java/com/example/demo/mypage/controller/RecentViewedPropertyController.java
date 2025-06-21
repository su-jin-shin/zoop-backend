package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.excel.ExcelGenerator;
import com.example.demo.common.excel.ExcelListResponse;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.excel.PropertyExcelMetaProvider;
import com.example.demo.common.exception.UserNotFoundException;

import com.example.demo.mypage.dto.*;

import com.example.demo.mypage.dto.RecentViewedPropertyRequest;

import com.example.demo.mypage.service.RecentViewedPropertyService;
import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/histories/recent-properties")
public class RecentViewedPropertyController {

    private final RecentViewedPropertyService recentViewedPropertyService;
    private final PropertyExcelMetaProvider propertyExcelMetaProvider;
    private final ExcelGenerator excelGenerator;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    public void saveRecentViewedProperty(@RequestBody RecentViewedPropertyRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = parseUserId(loginUser);
        if (userId == null) {
            throw new UserNotFoundException();
        }
        recentViewedPropertyService.save(userId, request.getPropertyId());
    }

    @GetMapping
    public ResponseEntity<?> getRecentViewedProperties(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = parseUserId(loginUser);
        if (userId == null) {
            throw new UserNotFoundException();
        }
        log.info("😀userId: "+userId);
        List<PropertyListItemDto> result = recentViewedPropertyService.getRecentViewedList(userId);
        log.info("📦 응답 데이터: {}", result);

        return ResponseEntity.ok(Map.of("recentViewedProperties", result));
    }

    @GetMapping("/map")
    public ResponseEntity<ExcelListResponse<PropertyExcelDto>> getRecentViewedPropertiesForMap(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = parseUserId(loginUser);
        log.info("😀userId: {}", userId);

        List<PropertyExcelDto> dtoList = recentViewedPropertyService.getRecentViewedPropertiesForExcel(userId);
        ExcelListResponse<PropertyExcelDto> response = ExcelListResponse.<PropertyExcelDto>builder()
                .countProperties(dtoList.size())
                .data(dtoList)
                .build();

        return ResponseEntity.ok(response); // ✅ 지도 + 엑셀 둘 다 활용 가능
    }

}
