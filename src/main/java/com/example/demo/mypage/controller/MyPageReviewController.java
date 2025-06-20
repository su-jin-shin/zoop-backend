package com.example.demo.mypage.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.mypage.dto.MyReviewResponse;
import com.example.demo.mypage.service.MyReviewService;
import com.example.demo.review.service.ReviewService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Spring DI로 주입된 객체는 외부 노출 위험 없음"
)
public class MyPageReviewController {

    private final MyReviewService myReviewService;
    private final ReviewService reviewService;

    private Long parseUserId(LoginUser loginUser) {
        try {
            return Long.valueOf(loginUser.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal LoginUser loginUser) {
        Long userId = parseUserId(loginUser);
        if (userId == null) {
            throw new UserNotFoundException();
        }
        log.debug("🔍 loginUser = {}", loginUser);
        List<MyReviewResponse> reviews = myReviewService.getMyReviews(userId);
        return ResponseEntity.ok().body(reviews);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteMyReview(@AuthenticationPrincipal LoginUser loginUser,
                                            @PathVariable Long reviewId) {
        Long userId = parseUserId(loginUser);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok(ResponseResult.success(
                HttpStatus.OK,
                SuccessMessage.REVIEW_DELETED.getMessage(), null));
    }
}
