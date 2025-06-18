package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.excel.ExcelGenerator;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.excel.PropertyExcelMetaProvider;
import com.example.demo.mypage.dto.PropertyMapResponse;
import com.example.demo.mypage.dto.MyPropertyPageResponse;
import com.example.demo.mypage.dto.MapPropertyDto;
import com.example.demo.mypage.service.BookmarkedPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/histories/bookmarked-properties")
public class BookmarkedPropertyController {

    private final BookmarkedPropertyService bookmarkedPropertyService;
    private final PropertyExcelMetaProvider propertyExcelMetaProvider;
    private final ExcelGenerator excelGenerator;

//    GET /mypage/histories/bookmarked-properties?page=2
    @GetMapping
    public ResponseEntity<MyPropertyPageResponse> getBookmarkedPropertiesByPageable(
            @AuthenticationPrincipal LoginUser loginUser,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());
        log.info("😀😀userId = {}", userId);
        return ResponseEntity.ok(bookmarkedPropertyService.getBookmarkedProperties(userId, pageable));
    }

    @GetMapping("/map")
    public ResponseEntity<PropertyMapResponse> getBookmarkedPropertiesForMap(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "recent") String sort
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());
        log.info("😀😀userId = {}", userId);
        // 1. 지도용 전체 좌표 리스트
        List<MapPropertyDto> mapDtos = bookmarkedPropertyService.getMapProperties(userId);

        // 2. 바텀시트용 페이지 목록
        MyPropertyPageResponse bottomSheet = bookmarkedPropertyService.getPagedProperties(userId, page, size, sort);

        return ResponseEntity.ok(PropertyMapResponse.builder()
                .mapProperties(mapDtos)
                .myPropertyPageResponse(bottomSheet)
                .build());
    }

    @GetMapping("/excel-export")
    public ResponseEntity<byte[]> exportBookmarkedPropertiesToExcel(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());
        log.info("😀😀userId = {}", userId);

        long start = System.currentTimeMillis();

        List<PropertyExcelDto> dtoList = bookmarkedPropertyService.getBookmarkedPropertiesForExcel(userId);

        List<String> headers = propertyExcelMetaProvider.getHeaders();
        List<Function<PropertyExcelDto, Object>> extractors = propertyExcelMetaProvider.getExtractors();

        ByteArrayInputStream in = excelGenerator.generateExcel(dtoList, headers, extractors);

        long end = System.currentTimeMillis();
        log.info("📤 [엑셀 생성 완료] 소요 시간 = {} ms", (end - start));

        String filename = URLEncoder.encode("찜한_매물_정보.xlsx", StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }

//    @GetMapping("/excel-export")
//    public ResponseEntity<ExcelResponseDto<PropertyExcelDto>> exportBookmarkedPropertiesToExcel(
//            @AuthenticationPrincipal LoginUser loginUser
//    ) {
//        Long userId = Long.valueOf(loginUser.getUsername());
//        log.info("😀😀userId = {}", userId);
//        long start = System.currentTimeMillis(); // 🔸 시간 시작
//        List<PropertyExcelDto> dtoList = bookmarkedPropertyService
//                .getBookmarkedPropertiesForExcel(userId);
//        long end = System.currentTimeMillis(); // 🔸 시간 종료
//        log.info("📤 [엑셀 생성 완료] 소요 시간 = {} ms", (end - start));
//
//        return ResponseEntity.ok(ExcelResponseDto.from(dtoList));
//    }



}
