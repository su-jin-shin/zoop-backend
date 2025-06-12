package com.example.demo.review.controller;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.service.ReviewCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentService commentService;

    /**
     * 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ReviewCommentResponse>> getComments(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        List<ReviewCommentResponse> comments = commentService.getComments(reviewId, currentUser);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<ReviewCommentResponse> createComment(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewCommentCreateRequest request,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        ReviewCommentResponse response = commentService.createComment(reviewId, currentUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ReviewCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid ReviewCommentUpdateRequest request,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        ReviewCommentResponse response = commentService.updateComment(commentId, currentUser, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * 댓글 좋아요 등록/해제
     */
    @PutMapping("/{commentId}/likes")
    public ResponseEntity<ReviewCommentLikeResponse> toggleLike(
            @PathVariable Long commentId,
            @RequestBody Map<String, Boolean> payload,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        Boolean isLiked = payload.get("isLiked");
        if (isLiked == null) {
            throw new IllegalArgumentException("isLiked 값이 필요합니다.");
        }

        ReviewCommentLikeResponse response = commentService.updateLikeStatus(commentId, currentUser, isLiked);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * 댓글 개수 조회
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCommentCount(
            @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(commentService.getCommentCount(reviewId));
    }

    /**
     * 댓글 좋아요 수 조회
     */
    @GetMapping("/{commentId}/likes/counts")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(commentService.getLikeCount(commentId));
    }

    /**
     * 댓글 좋아요 여부 조회
     */
    @GetMapping("/{commentId}/likes")
    public ResponseEntity<Boolean> isLiked(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserInfo currentUser
    ) {
        boolean liked = commentService.isLiked(commentId, currentUser);
        return ResponseEntity.ok(liked);
    }
}
