package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.MyPageHomeResponse;
import com.example.demo.mypage.service.MyHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/home")
public class MyPageHomeController {

    private final MyHomeService myHomeService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult> getMyPageHome(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = parseUserId(loginUser);

        MyPageHomeResponse response = myHomeService.getHomeInfo(userId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.GET_MYPAGE_HOME.getMessage(),
                        response
                )
        );
    }
}
