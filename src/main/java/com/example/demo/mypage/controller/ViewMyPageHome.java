package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.service.RecentViewedPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class ViewMyPageHome {

    private final RecentViewedPropertyService recentViewedPropertyService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/histories/recent-properties/view")
    public String getRecentViewedItems() {
        return "mypage/home";
    }

    @GetMapping("/histories/bookmarked-properties/view")
    public String getBookmarkedItems() {
        return "mypage/home";
    }

}
