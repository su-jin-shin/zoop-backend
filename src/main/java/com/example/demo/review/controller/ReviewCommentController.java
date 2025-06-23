package com.example.demo.review.controller;

import com.example.demo.review.service.ReviewCommentService;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.common.response.SuccessMessage;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.review.dto.ReviewComment.*;
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


    private final ReviewCommentService commentService;


    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<?> getComments(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal LoginUser loginUser) {

        Long userId = Long.valueOf(loginUser.getUsername());

        List<ReviewCommentCreateResponse> comments = commentService.getComments(reviewId, userId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.GET_COMMENT_LIST_SUCCESS.getMessage(), comments));
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewCommentCreateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewCommentCreateResponse response = commentService.createComment(reviewId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success(HttpStatus.CREATED, SuccessMessage.COMMENT_CREATED.getMessage(), response));
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid ReviewCommentUpdateRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        ReviewCommentCreateResponse response = commentService.updateComment(commentId, userId, request);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_UPDATED.getMessage(), response));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ResponseResult.success(HttpStatus.OK, SuccessMessage.COMMENT_DELETED.getMessage(), null));
    }

    // 댓글 좋아요 등록/해제
    @PutMapping("/{commentId}/likes")
    public ResponseEntity<?> updateCommentLikeStatus(
            @PathVariable Long commentId,
            @RequestBody ReviewCommentLikeRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        Boolean isLiked = request.getIsLiked();
        if (isLiked == null) {
            throw new InvalidRequestException();
        }

        ReviewCommentLikeResponse response = commentService.updateLikeStatus(commentId, isLiked, userId);
        HttpStatus status = isLiked ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ResponseResult.success(status, SuccessMessage.COMMENT_LIKE_STATUS_UPDATED.getMessage(), response));
    }


    // 댓글 좋아요 수 조회
    @GetMapping("/{commentId}/likes/counts")
    public ResponseEntity<?> getCommentLikeCount(@PathVariable Long commentId) {
        CommentLikeCountResponse response = commentService.commentLikeCount(commentId);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, "요청이 정상적으로 처리되었습니다.", response)
        );
    }



    // 댓글 좋아요 여부 조회
    @GetMapping("/{commentId}/likes")
    public ResponseEntity<?> getCommentLikeStatus(
            @PathVariable Long commentId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername());

        CommentLikeStatusResponse response = commentService.getLikeStatus(commentId, userId);

        return ResponseEntity.ok(
                ResponseResult.success(HttpStatus.OK, "요청이 정상적으로 처리되었습니다.", response)
        );
    }



}



