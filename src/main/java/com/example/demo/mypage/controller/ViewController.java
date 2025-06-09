package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyPageAccountResponse;
import com.example.demo.mypage.service.MypageAccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    MypageAccountService myPageService;

    @GetMapping("/mypage/nickname-edit")
    public String showNicknameEditPage() {
        return "nickname-edit";
    }

    @GetMapping("/mypage/account/view")
    public String accountView(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = Long.valueOf(loginUser.getUsername());
        MyPageAccountResponse response = myPageService.getAccountInfo(userId);

        model.addAttribute("email", response.getEmail());
        model.addAttribute("nickname", response.getNickname());
        model.addAttribute("profileImageUrl", response.getProfileImageUrl());

        return "mypage/account";
    }
}
