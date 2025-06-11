package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.service.MyCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageCommentController {

    private final MyCommentService commentService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getMyComments(@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);
        List<MyCommentResponse> comments = commentService.getMyComments(userId);

        return ResponseEntity.ok(comments);
    }
}
