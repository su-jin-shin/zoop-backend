package com.example.demo.auth.controller;

import com.example.demo.auth.dto.WithdrawRequestDto;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.service.WithdrawServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawServiceImpl withdrawService;


    // 탈퇴 + 사유 입력 (POST 방식)

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody WithdrawRequestDto request) {

        withdrawService.withdraw(Long.valueOf(loginUser.getUsername()), request.getWithdrawReason());
        return ResponseEntity.ok("탈퇴가 정상적으로 처리되었습니다.");
    }
}
