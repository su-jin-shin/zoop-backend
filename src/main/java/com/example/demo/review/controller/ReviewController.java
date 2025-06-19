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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 리스트 조회(전체)
    @GetMapping("/{propertyId}")
    public ResponseEntity<?> getReviews(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewListResponse response = reviewService.getReviews(userId, propertyId);

        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_FETCHED.getMessage(), response));
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long propertyId,
            @RequestBody @Valid ReviewCreateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        ReviewCreateResponse response = reviewService.createReview(propertyId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success(HttpStatus.CREATED, SuccessMessage.REVIEW_CREATED.getMessage(), response));
    }

    @PatchMapping("/{reviewId}")  //성공
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        ReviewCreateResponse response = reviewService.updateReview(reviewId, request, userId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_UPDATED.getMessage(), response));
    }

    @DeleteMapping("/{reviewId}")  //성공
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_DELETED.getMessage(), null));
    }

    @PutMapping("/{reviewId}/likes")
    public ResponseEntity<?> updateLikeStatus(
            @PathVariable Long reviewId,
            @RequestBody ReviewLikeRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }  //없어도 됨 --> 추후 삭제하고 @AuthenticationPrincipal이 null이면 exception hander에서 잡기

        Boolean isLiked = request.getIsLiked();
        if (isLiked == null) {
            throw new InvalidRequestException();
        }

        ReviewLikeResponse response = reviewService.updateLikeStatus(reviewId, isLiked, userId);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ResponseResult.success(status, SuccessMessage.REVIEW_LIKED.getMessage(), response));
    }

    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<?> getLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.LOGIN_REQUIRED.getMessage(), null));
        }

        // DTO 바로 받아서 응답
        ReviewLikeResponse response = reviewService.getLikeStatus(reviewId, userId);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_LIKE_STATUS_FETCHED.getMessage(), response)
        );
    }


    @GetMapping("/{reviewId}/likes/counts")
    public ResponseEntity<?> getLikeCount(@PathVariable Long reviewId) {
        long likeCount = reviewService.getLikeCount(reviewId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("reviewId", reviewId);
        responseData.put("likeCount", likeCount);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_LIKE_COUNT_FETCHED.getMessage(), responseData)
        );
    }


    @GetMapping("/{reviewId}/comments/counts")
    public ResponseEntity<?> getCommentCount(@PathVariable Long reviewId) {
        long count = reviewService.getCommentCount(reviewId);
        CommentCountResponse response = CommentCountResponse.builder()
                .reviewId(reviewId).commentCount(count).build();
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_COUNT_FETCHED.getMessage(), response));
    }


    //내 리뷰 조회
    @GetMapping("/my")
    public ResponseEntity<?> getMyReviews(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        var myReviews = reviewService.getMyReviews(userId);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, "내 리뷰 목록 조회 성공", myReviews)
        );
    }



}



