package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyReviewResponseDto;
import com.example.demo.mypage.repository.MyReviewRepository;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import com.example.demo.mypage.service.MyReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageReviewController {

    private final MyReviewService myReviewService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);
        List<MyReviewResponseDto> reviews = myReviewService.getMyReviews(userId);
        return ResponseEntity.ok().body(reviews);
    }
}
