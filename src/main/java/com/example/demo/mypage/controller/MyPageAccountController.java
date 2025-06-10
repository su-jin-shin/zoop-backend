package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyPageAccountResponse;
import com.example.demo.mypage.dto.NicknameUpdateRequest;
import com.example.demo.mypage.dto.ProfileImageResponse;
import com.example.demo.mypage.service.MypageAccountService;
import com.example.demo.mypage.service.NicknameService;
import com.example.demo.mypage.service.ProfileImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageAccountController {

    private final MypageAccountService myPageService;
    private final NicknameService nicknameService;
    private final ProfileImageService profileImageService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

//    Long userId = 1L;

    // 닉네임 수정
    @PatchMapping("/user-nickname")
    public ResponseEntity<?> updateNickname(
            @RequestBody @Valid NicknameUpdateRequest request
            , @AuthenticationPrincipal LoginUser loginUser) {
//        System.out.println("😂😂"+loginUser); // >>> 테스트 중 아직 인증 부분에 문제가 있음
//
//        System.out.println("😂😂"+loginUser.getUsername());
        Long userId = parseUserId(loginUser);
//        Long userId = 1L;
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MyPageAccountController.ErrorResponse("로그인이 필요합니다."));
        }

        if (nicknameService.isNicknameDuplicate(request.getNickname())) {
            return ResponseEntity.badRequest().body(
                    new MyPageAccountController.ErrorResponse("닉네임 중복 확인을 먼저 해주세요.")
            );
        }

        nicknameService.updateNickname(userId, request.getNickname());

        return ResponseEntity.ok(
                new MyPageAccountController.SuccessResponse("요청이 정상적으로 처리되었습니다.")
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
                new MyPageAccountController.NicknameCheckResponse(
                        isDuplicated, message));
    }

    // 프로필 이미지 수정
    @PostMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam("profileImage") MultipartFile file) {

        Long userId = parseUserId(loginUser);
//        Long userId = 1L;
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("로그인 후 다시 시도해주세요."));
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("이미지 파일이 없습니다."));
        }

        try {
            String savedUrl = profileImageService.uploadAndSave(userId, file);
            return ResponseEntity.ok(new ProfileImageResponse(savedUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("파일 업로드에 실패했습니다."));
        }
    }

    // 프로필 이미지 삭제 (DB에 저장되어 있는 기본 이미지로 재설정)
    @PatchMapping("/profile-image/reset")
    public ResponseEntity<?> deleteProfileImage(
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);
//        Long userId = 1L;
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("로그인 후 다시 시도해주세요."));
        }

        String defaultUrl = profileImageService.resetToDefaultImage(userId);
        return ResponseEntity.ok().body(new ProfileImageResponse(defaultUrl));
    }

    // 회원 탈퇴
//    @DeleteMapping("/withdraw")

    // 내 정보 조회
    @GetMapping("/account")
    public ResponseEntity<?> getAccountInfo(@AuthenticationPrincipal LoginUser loginUser, Model model) {

        Long userId = parseUserId(loginUser);
//        Long userId = 1L;
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("로그인 후 다시 시도해주세요."));
        }
        MyPageAccountResponse response = myPageService.getAccountInfo(userId);


        return ResponseEntity.ok(response);
    }


    // 응답 DTO
    record SuccessResponse(String message) {}
    record ErrorResponse(String message) {}
    record NicknameCheckResponse(boolean isDuplicated, String message) {}

}
