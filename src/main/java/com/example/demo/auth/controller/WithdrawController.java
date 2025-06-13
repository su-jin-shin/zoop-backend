package com.example.demo.auth.controller;

import com.example.demo.auth.dto.WithdrawRequestDto;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.service.WithdrawServiceImpl;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.common.response.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawServiceImpl withdrawService;

    @PostMapping("/withdraw")
    public ResponseEntity<ResponseResult> withdraw(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody WithdrawRequestDto request) {

        if (loginUser == null) {
            throw new UnauthorizedAccessException(); // 403 처리
        }

        withdrawService.withdraw(Long.valueOf(loginUser.getUsername()), request.getWithdrawReason());

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        "탈퇴가 정상적으로 처리되었습니다.",
                        null
                )
        );
    }
}
