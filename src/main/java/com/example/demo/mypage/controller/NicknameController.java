package com.example.demo.mypage.controller;

import com.example.demo.mypage.dto.NicknameUpdateRequest;
import com.example.demo.mypage.service.NicknameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Validated
public class NicknameController {

    private final NicknameService nicknameService;

    // 닉네임 수정
    @PatchMapping("/user-nickname")
    public ResponseEntity<?> updateNickname(
            @RequestBody @Valid NicknameUpdateRequest request
            , @AuthenticationPrincipal Long userId) {

//        Long userId = 1L;

        if (!Boolean.TRUE.equals(request.getIsChecked())) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("닉네임 중복 확인을 먼저 해주세요.")
            );
        }

        nicknameService.updateNickname(userId, request.getNickname());

        return ResponseEntity.ok(
                new SuccessResponse("요청이 정상적으로 처리되었습니다.")
        );
    }

    // 닉네임 중복 확인
    @GetMapping("/check-user-nickname")
    public ResponseEntity<?> checkNicknameDuplicate(
            @RequestParam("nickname") String nickname) {

        boolean isDuplicated = nicknameService.isNicknameDuplicate(nickname);

        String message = isDuplicated
                ? "이미 사용 중인 닉네임입니다."
                : "해당 닉네임을 사용할 수 있습니다.";

        return ResponseEntity.ok(
                new NicknameCheckResponse(
                        isDuplicated, message));
    }

    // 응답 DTO
    record SuccessResponse(String message) {}
    record ErrorResponse(String message) {}
    record NicknameCheckResponse(boolean isDuplicated, String message) {}
}
