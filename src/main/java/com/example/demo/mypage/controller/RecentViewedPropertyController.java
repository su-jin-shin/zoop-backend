package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.RecentViewedPropertyRequest;
import com.example.demo.mypage.dto.MyPropertyResponse;
import com.example.demo.mypage.service.RecentViewedPropertyService;
import com.example.demo.property.dto.PropertyListItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
