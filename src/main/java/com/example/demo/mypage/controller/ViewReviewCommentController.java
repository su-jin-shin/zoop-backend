package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyReviewResponseDto;
import com.example.demo.mypage.service.MyReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewReviewCommentController {

    private final MyReviewService myReviewService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/mypage/reviews/view")
    public String getMyReviewPage(Model model/*, @AuthenticationPrincipal LoginUser loginUser*/) {

//        Long userId = parseUserId(loginUser);
        Long userId = 1L;
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("🎯 principal: {}", principal);
//        System.out.println("🔎🔎🔎🔎🔎로그인 유저 id: " + userId);

        List<MyReviewResponseDto> reviews = myReviewService.getMyReviews(userId);
        model.addAttribute("reviews", reviews);
        return "mypage/myReviewList";
    }

}
