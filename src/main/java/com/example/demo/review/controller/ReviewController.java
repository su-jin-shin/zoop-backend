package com.example.demo.review.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.exception.PropertyNotFoundException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.review.dto.Review.Request.ReviewCreateRequest;
import com.example.demo.review.dto.Review.Request.ReviewLikeRequest;
import com.example.demo.review.dto.Review.Request.ReviewUpdateRequest;
import com.example.demo.review.dto.Review.Response.*;
import com.example.demo.review.service.ReviewService;
import com.example.demo.review.service.ReviewSummaryService;
import com.fasterxml.jackson.databind.JsonNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewSummaryService reviewSummaryService;
    private final RestTemplate restTemplate;                        // 외부 AI 서버 요청용
    private final ObjectMapper objectMapper;

    // 리뷰 목록 조회
    @GetMapping("/{propertyId}")
    public ResponseEntity<?> getReviews(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal LoginUser loginUser){

        Long userId = Long.valueOf(loginUser.getUsername()); //로그인 유저 정보 추출
        ReviewListResponse response = reviewService.getReviews(userId, propertyId);
        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_FETCHED.getMessage(), response));
    }

    // 리뷰 작성
    @PostMapping("/{propertyId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long propertyId,
            @RequestBody @Valid ReviewCreateRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewCreateResponse response = reviewService.createReview(propertyId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success(HttpStatus.CREATED, SuccessMessage.REVIEW_CREATED.getMessage(), response));
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewCreateResponse response = reviewService.updateReview(reviewId, request, userId);
        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_UPDATED.getMessage(), response));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_DELETED.getMessage(), null));
    }

    // 리뷰 좋아요 등록/해제
    @PutMapping("/{reviewId}/likes")
    public ResponseEntity<?> updateLikeStatus(
            @PathVariable Long reviewId,
            @RequestBody ReviewLikeRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        Boolean isLiked = request.getIsLiked();
        if (isLiked == null) {throw new InvalidRequestException();}

        ReviewLikeResponse response = reviewService.updateLikeStatus(reviewId, isLiked, userId);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ResponseResult.success(status, SuccessMessage.REVIEW_LIKED.getMessage(), response));
    }

    // 좋아요 여부 조회
    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<?> getLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewLikeResponse response = reviewService.getLikeStatus(reviewId, userId);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.REVIEW_LIKE_STATUS_FETCHED.getMessage(), response)
        );
    }

    // 좋아요 수 조회
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

    // 댓글 수 조회
    @GetMapping("/{reviewId}/comments/counts")
    public ResponseEntity<?> getCommentCount(@PathVariable Long reviewId) {

        long count = reviewService.getCommentCount(reviewId);
        CommentCountResponse response = CommentCountResponse.builder()
                .reviewId(reviewId).commentCount(count).build();
        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_COUNT_FETCHED.getMessage(), response));
    }

//    // AI 리뷰 요약 조회
//    @GetMapping("/{propertyId}/summary")
//    public ResponseEntity<?> getSummary(@PathVariable Long propertyId) {
//
//        AiSummaryResponse summary = reviewSummaryService.getOrFetchSummary(propertyId);
//        return ResponseEntity.ok(
//                ResponseResult.success(HttpStatus.OK, "요청이 정상적으로 처리되었습니다.", summary)
//        );
//    }
    @GetMapping("/{propertyId}/summary")
    public ResponseEntity<?> getSummary(@PathVariable Long propertyId) {
        try {
            AiSummaryResponse summary = reviewSummaryService.getSummaryFromAi(propertyId); // 또는 getOrFetchSummary()
            return ResponseEntity.ok(
                    ResponseResult.success(HttpStatus.OK, "요약 정보를 성공적으로 불러왔습니다.", summary)
            );
        } catch (PropertyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseResult.failed(HttpStatus.NOT_FOUND, FailedMessage.PROPERTY_NOT_FOUND.getMessage(), null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseResult.failed(HttpStatus.INTERNAL_SERVER_ERROR, FailedMessage.INTERNAL_SERVER_ERROR.getMessage(), null)
            );
        }
    }






}



