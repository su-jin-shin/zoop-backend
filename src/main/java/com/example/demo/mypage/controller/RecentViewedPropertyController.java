package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.excel.ExcelListResponse;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.common.exception.UserNotFoundException;

import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.RecentViewedPropertyRequest;

import com.example.demo.mypage.service.RecentViewedPropertyService;
import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/histories/recent-properties")
public class RecentViewedPropertyController {

    private final RecentViewedPropertyService recentViewedPropertyService;

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

        recentViewedPropertyService.save(userId, request.getPropertyId());
    }

    @GetMapping
    public ResponseEntity<ResponseResult> getRecentViewedProperties(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = parseUserId(loginUser);

        List<PropertyListItemDto> result = recentViewedPropertyService.getRecentViewedList(userId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.GET_RECENT_VIEWED_PROPERTIES.getMessage(),
                        Map.of("recentViewedProperties", result))
                );
    }

    @GetMapping("/map")
    public ResponseEntity<ResponseResult> getRecentViewedPropertiesForMap(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = parseUserId(loginUser);

        List<PropertyExcelDto> dtoList = recentViewedPropertyService.getRecentViewedPropertiesForExcel(userId);
        ExcelListResponse<PropertyExcelDto> response = ExcelListResponse.<PropertyExcelDto>builder()
                .countProperties(dtoList.size())
                .data(dtoList)
                .build();

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.RECENT_VIEWED_PROPERTIES_FOR_MAP_FETCHED.getMessage(),
                        response
                )
        ); // ✅ 지도 + 엑셀 둘 다 활용 가능
    }

}
