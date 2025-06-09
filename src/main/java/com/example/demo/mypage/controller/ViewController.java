package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyPageAccountResponse;
import com.example.demo.mypage.service.MypageAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final MypageAccountService myPageService;

    @GetMapping("/mypage/nickname-edit")
    public String showNicknameEditPage() {
        return "mypage/nickname-edit";
    }

    @GetMapping("/mypage/image-url-edit")
    public String showImageUrlEditPage() {
        return "mypage/image-url-edit";
    }

    @GetMapping("/mypage/account/view")
    public String accountView(Model model/*, @AuthenticationPrincipal LoginUser loginUser*/) {

        Long userId = 1L; // 테스트용 사용자 ID

//        if (loginUser == null) {
//            return "redirect:/login"; // 또는 에러 페이지
//        }
//        Long userId = Long.valueOf(loginUser.getUsername());
        MyPageAccountResponse response = myPageService.getAccountInfo(userId);

        model.addAttribute("email", response.getEmail());
        model.addAttribute("nickname", response.getNickname());
        model.addAttribute("profileImageUrl", response.getProfileImageUrl());

        return "mypage/account";
    }
}
