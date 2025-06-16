package com.example.demo.review.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.service.ReviewCommentService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentController {

    private static final Logger log = LoggerFactory.getLogger(ReviewCommentController.class);
    private final ReviewCommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long reviewId, @AuthenticationPrincipal LoginUser loginUser) {
        List<ReviewCommentResponse> comments = commentService.getComments(reviewId, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.GET_COMMENT_LIST_SUCCESS.getMessage(), comments));
    }

    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewCommentCreateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.USER_NOT_FOUND.getMessage(), null));
        }

        ReviewCommentResponse response = commentService.createComment(reviewId, loginUser, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success(HttpStatus.CREATED, SuccessMessage.COMMENT_CREATED.getMessage(), response));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid ReviewCommentUpdateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        ReviewCommentResponse response = commentService.updateComment(commentId, loginUser, request);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_UPDATED.getMessage(), response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        commentService.deleteComment(commentId, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_DELETED.getMessage(), null));
    }

    @PutMapping("/{commentId}/likes")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long commentId,
            @RequestBody Map<String, Boolean> payload,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.failed(HttpStatus.UNAUTHORIZED, FailedMessage.USER_NOT_FOUND.getMessage(), null));
        }

        Boolean isLiked = payload.get("isLiked");
        if (isLiked == null) {
            throw new InvalidRequestException();
        }

        var response = commentService.updateLikeStatus(commentId, loginUser, isLiked);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ResponseResult.success(status, SuccessMessage.COMMENT_LIKE_STATUS_UPDATED.getMessage(), response));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCommentCount(@PathVariable Long reviewId) {
        Long count = commentService.getCommentCount(reviewId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.GET_COMMENT_COUNT.getMessage(), count));
    }

    @GetMapping("/{commentId}/likes/counts")
    public ResponseEntity<?> getLikeCount(@PathVariable Long commentId) {
        Long count = commentService.getLikeCount(commentId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.GET_COMMENT_LIKE_COUNT.getMessage(), count));
    }

    @GetMapping("/{commentId}/likes")
    public ResponseEntity<?> isLiked(@PathVariable Long commentId, @AuthenticationPrincipal LoginUser loginUser) {
        boolean liked = commentService.isLiked(commentId, loginUser);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.GET_COMMENT_LIKE_STATUS.getMessage(), liked));
    }
}



