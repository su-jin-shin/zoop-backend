package com.example.demo.auth.controller;

import com.example.demo.auth.dto.WithdrawRequestDto;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.service.WithdrawServiceImpl;
import com.example.demo.common.exception.UnauthorizedAccessException;
import com.example.demo.common.response.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawServiceImpl withdrawService;

    @DeleteMapping("/withdraw")
    public ResponseEntity<ResponseResult> withdraw(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody WithdrawRequestDto request,
            HttpServletRequest httpReq) {

        if (loginUser == null) throw new UnauthorizedAccessException();

        withdrawService.withdraw(Long.valueOf(loginUser.getUsername()), request.getWithdrawReason());

        // 1) Spring Security 컨텍스트 초기화
        SecurityContextHolder.clearContext();
        // 2) 세션 무효화 (있을 때만)
        HttpSession sess = httpReq.getSession(false);
        if (sess != null) sess.invalidate();


        // 로그아웃처럼 쿠키 삭제 추가
        ResponseCookie delAccess = ResponseCookie.from("ACCESS_TOKEN", "")
                .domain("zoopzoop.shop").path("/").secure(true)
                .sameSite("None").httpOnly(false).maxAge(0).build();

        ResponseCookie delRefresh = ResponseCookie.from("REFRESH_TOKEN", "")
                .domain("zoopzoop.shop").path("/").secure(true)
                .sameSite("None").httpOnly(false).maxAge(0).build();

        ResponseCookie delKakao = ResponseCookie.from("KAKAO_ACCESS", "")
                .domain("zoopzoop.shop").path("/").secure(true)
                .sameSite("None").httpOnly(false).maxAge(0).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, delAccess.toString())
                .header(HttpHeaders.SET_COOKIE, delRefresh.toString())
                .header(HttpHeaders.SET_COOKIE, delKakao.toString())
                .body(ResponseResult.success(
                        HttpStatus.OK,
                        "탈퇴가 정상적으로 처리되었습니다.",
                        null
                ));
    }
}
