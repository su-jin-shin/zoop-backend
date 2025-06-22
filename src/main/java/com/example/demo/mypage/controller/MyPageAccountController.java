package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.DuplicatedNicknameException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.MyPageAccountResponse;
import com.example.demo.mypage.dto.NicknameCheckResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.common.response.FailedMessage.CHECK_AVAILABLE_NICKNAME;
import static com.example.demo.common.response.FailedMessage.DUPLICATED_NICKNAME;

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

    // 닉네임 수정 완료
    @PatchMapping("/user-nickname")
    public ResponseEntity<?> updateNickname(
            @RequestBody @Valid NicknameUpdateRequest request
            , @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);

        if (nicknameService.isNicknameDuplicate(request.getNickname())) {
            throw new DuplicatedNicknameException();
        }

        nicknameService.updateNickname(userId, request.getNickname());

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.UPDATE_NICKNAME_SUCCESS.getMessage(),
                        null
                )
        );
    }

    // 닉네임 중복 확인
    @GetMapping("/check-user-nickname")
    public ResponseEntity<?> checkNicknameDuplicate(
            @RequestParam("nickname") String nickname) {

        boolean isDuplicated = nicknameService.isNicknameDuplicate(nickname);

        String message = isDuplicated
                ? DUPLICATED_NICKNAME.getMessage()
                : CHECK_AVAILABLE_NICKNAME.getMessage();

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        message,
                        new NicknameCheckResponse(isDuplicated)
                ));
    }

    // 프로필 이미지 수정
    @PostMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam("profileImageUrl") MultipartFile file) {

        Long userId = parseUserId(loginUser);

        try {
            String savedUrl = profileImageService.uploadAndSave(userId, file);
            return ResponseEntity.ok(ResponseResult.success(
                    HttpStatus.OK,
                    SuccessMessage.PROFILE_IMAGE_UPDATED.getMessage(),
                    new ProfileImageResponse(savedUrl)
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseResult.failed(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            FailedMessage.FILE_UPLOAD_FAILED.getMessage(),
                            null
                    )
            );
        }
    }

    // 프로필 이미지 삭제 (DB에 저장되어 있는 기본 이미지로 재설정)
    @PatchMapping("/profile-image/reset")
    public ResponseEntity<?> deleteProfileImage(
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);
        String defaultUrl = profileImageService.resetToDefaultImage(userId);
        return ResponseEntity.ok(ResponseResult.success(
                HttpStatus.OK,
                SuccessMessage.PROFILE_IMAGE_RESET.getMessage(),
                new ProfileImageResponse(defaultUrl)
        ));
    }

    // 내 정보 조회
    @GetMapping("/account")
    public ResponseEntity<?> getAccountInfo(@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);

        MyPageAccountResponse response = myPageService.getAccountInfo(userId);

        return ResponseEntity.ok(ResponseResult.success(
                HttpStatus.OK,
                SuccessMessage.GET_ACCOUNT_INFO_SUCCESS.getMessage(),
                response
        ));
    }

}
