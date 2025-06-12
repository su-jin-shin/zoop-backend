package com.example.demo.review.controller;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.dto.Review.*;
import com.example.demo.review.service.ReviewService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewController {

    private final ReviewService reviewService;


    /**
     * 리뷰 목록 조회 (정렬 + 필터링 + 페이징)
     * ex) /reviews?complexId=1&sort=like&page=0&size=10&onlyMine=true
     */
    @GetMapping("/{propertyId}")
    public ReviewListResponse getReviews(
            @RequestParam(required = false) Long complexId,
            @RequestParam(required = false) Long propertyId,
            @RequestParam(defaultValue = "like") String sort,
            @RequestParam(defaultValue = "false") boolean isMine,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        return reviewService.getReviews(
                complexId,
                propertyId,
                sort,
                isMine,
                currentUser,
                page,
                size
        );
    }


    /**
     * 리뷰 단일 조회  -->api 명세서에는 없는듯
     */
    @GetMapping("/{reviewId}")
    public ReviewResponse getReviewById(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        return reviewService.getReviewById(reviewId, currentUser);
    }


    //리뷰 작성
    @PostMapping("/{propertyId}")
    public ReviewResponse createReview(
            @RequestParam Long propertyId,
            @RequestBody @Valid ReviewCreateRequest request,
            @RequestAttribute("user") UserInfo currentUser
    ) {
        return reviewService.createReview(propertyId, request, currentUser);
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    public ReviewResponse updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @RequestAttribute("user") UserInfo currentUser
    ) {
        return reviewService.updateReview(reviewId, request, currentUser);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReview(
            @PathVariable Long reviewId,
            @RequestAttribute("user") UserInfo currentUser
    ) {
        reviewService.deleteReview(reviewId, currentUser);
    }

    //좋아요 등록/해제
    @PutMapping("/{reviewId}/likes")
    public ResponseEntity<ReviewLikeResponse> updateLikeStatus(
            @PathVariable Long reviewId,
            @RequestBody Map<String, Boolean> payload,
            @AuthenticationPrincipal UserInfo currentUser) {


        Boolean isLiked = payload.get("isLiked");

        if(isLiked == null) {
            throw new IllegalArgumentException("isLiked 값이 필요합니다.");
        }

        ReviewLikeResponse response = reviewService.updateLikeStatus(reviewId, isLiked, currentUser);

        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;

        return ResponseEntity.status(status).body(response);
    }

    //좋아요 여부 조회
    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<Boolean> getLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserInfo currentUser) {
        boolean liked = reviewService.isLiked(reviewId, currentUser);
        return ResponseEntity.ok(liked);
    }

    //좋아요 개수 조회
    @GetMapping("/{reviewId}/likes/counts")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getLikeCount(reviewId));
    }

    // 댓글 수 조회
    @GetMapping("/{reviewId}/comments/counts")
    public ResponseEntity<CommentCountResponse> getCommentCount(@PathVariable Long reviewId) {
        long count = reviewService.getCommentCount(reviewId);
        return ResponseEntity.ok(
                CommentCountResponse.builder()
                        .reviewId(reviewId)
                        .commentCount(count)
                        .build()
        );
    }
}
