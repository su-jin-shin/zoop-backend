package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.service.MyCommentService;
import com.example.demo.review.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring DI로 주입된 Service는 외부 노출 위험 없음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageCommentController {

    private final MyCommentService myCommentService;
    private final ReviewCommentService commentService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null; 
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<ResponseResult> getMyComments(@AuthenticationPrincipal LoginUser loginUser) {

        Long userId = parseUserId(loginUser);
        List<MyCommentResponse> comments = myCommentService.getMyComments(userId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        SuccessMessage.GET_COMMENT_LIST_SUCCESS.getMessage(),
                        comments
                )
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteMyReview(@AuthenticationPrincipal LoginUser loginUser,
                                            @PathVariable Long commentId) {

        Long userId = parseUserId(loginUser);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        commentService.deleteComment(commentId, userId);

        return ResponseEntity.ok(ResponseResult.success(
                HttpStatus.OK,
                SuccessMessage.COMMENT_DELETED.getMessage(),
                null));
    }
}
