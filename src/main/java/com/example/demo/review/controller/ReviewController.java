package com.example.demo.review.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.service.ReviewService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{propertyId}")
    public ResponseEntity<?> getReviews(
            @RequestParam(required = false) Long complexId,
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "like") String sort,
            @RequestParam(defaultValue = "false") boolean isMine,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (isMine && loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        ReviewListResponse response = reviewService.getReviews(
                complexId, propertyId, sort, isMine, loginUser, page, size);

        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_FETCHED.getMessage(), response));
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long propertyId,
            @RequestBody @Valid ReviewCreateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        ReviewResponse response = reviewService.createReview(propertyId, request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success(HttpStatus.CREATED, SuccessMessage.REVIEW_CREATED.getMessage(), response));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        ReviewResponse response = reviewService.updateReview(reviewId, request, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_UPDATED.getMessage(), response));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        reviewService.deleteReview(reviewId, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_DELETED.getMessage(), null));
    }

    @PutMapping("/{reviewId}/likes")
    public ResponseEntity<?> updateLikeStatus(
            @PathVariable Long reviewId,
            @RequestBody Map<String, Boolean> payload,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        Boolean isLiked = payload.get("isLiked");
        if (isLiked == null) {
            throw new InvalidRequestException();
        }

        ReviewLikeResponse response = reviewService.updateLikeStatus(reviewId, isLiked, loginUser);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ResponseResult.success(status, SuccessMessage.REVIEW_LIKED.getMessage(), response));
    }

    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<?> getLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        boolean liked = reviewService.isLiked(reviewId, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_LIKE_STATUS_FETCHED.getMessage(), liked));
    }

    @GetMapping("/{reviewId}/likes/counts")
    public ResponseEntity<?> getLikeCount(@PathVariable Long reviewId) {
        long count = reviewService.getLikeCount(reviewId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_LIKE_COUNT_FETCHED.getMessage(), count));
    }

    @GetMapping("/{reviewId}/comments/counts")
    public ResponseEntity<?> getCommentCount(@PathVariable Long reviewId) {
        long count = reviewService.getCommentCount(reviewId);
        CommentCountResponse response = CommentCountResponse.builder()
                .reviewId(reviewId).commentCount(count).build();
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_COUNT_FETCHED.getMessage(), response));
    }
}



