package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.excel.ExcelGenerator;
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
    public ResponseEntity<PropertyMapResponse> getRecentViewedPropertiesForMap(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = parseUserId(loginUser);
        PropertyMapResponse response = recentViewedPropertyService.getRecentViewedPropertiesWithMap(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/excel-export")
    public ResponseEntity<byte[]> exportRecentViewedPropertiesToExcel(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());
        log.info("😀😀userId = {}", userId);

        long start = System.currentTimeMillis();

        List<PropertyExcelDto> dtoList = recentViewedPropertyService.getRecentViewedPropertiesForExcel(userId);

        List<String> headers = propertyExcelMetaProvider.getHeaders();
        List<Function<PropertyExcelDto, Object>> extractors = propertyExcelMetaProvider.getExtractors();

        ByteArrayInputStream in = excelGenerator.generateExcel(dtoList, headers, extractors);

        long end = System.currentTimeMillis();
        log.info("📤 [엑셀 생성 완료] 소요 시간 = {} ms", (end - start));

        String filename = URLEncoder.encode("최근_본_매물_정보.xlsx", StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }
}
