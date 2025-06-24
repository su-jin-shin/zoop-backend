
package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.*;
import com.example.demo.review.domain.Review;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import com.example.demo.review.dto.ReviewComment.*;
import com.example.demo.review.mapper.ReviewCommentMapper;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import com.example.demo.review.repository.ReviewCommentRepository;
import com.example.demo.review.repository.ReviewRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * ReviewCommentService
 *
 * 리뷰에 달린 **댓글**과 관련된 기능을 담당하는 서비스

 * - 댓글 목록 조회, 작성, 수정, 삭제 (Soft Delete 방식)
 * - 댓글 좋아요 등록 및 취소, 좋아요 여부 및 개수 조회
 * - 리뷰가 삭제되면 댓글 조회 불가
 * - 댓글 좋아요는 사용자 + 댓글 조합으로 관리 (중복 좋아요 방지)
 * - 댓글은 Soft Delete 방식으로 `deletedAt` 필드 기준 관리
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentService {

    // 의존성 주입
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;
    private final ReviewCommentLikeRepository likeRepository;
    private final ReviewCommentMapper commentMapper;
    private final UserInfoRepository userInfoRepository;

    /**
     * 댓글 목록 조회
     * - 특정 리뷰에 작성된 댓글 리스트를 반환
     * - 각 댓글에 대해 좋아요 수, 내가 누른 여부, 내가 쓴 댓글 여부를 포함
     */
    public List<ReviewCommentCreateResponse> getComments(Long reviewId, Long userId) {
        // 유저 및 리뷰 유효성 확인
        UserInfo loginUser = getUserInfo(userId);
        getReview(reviewId);

        // 댓글 리스트 조회 
        List<ReviewComment> comments = commentRepository.findByReviewId(reviewId);
        List<Long> commentIds = comments.stream()
                .map(ReviewComment::getId)
                .toList();
        
       // 각 댓글의 좋아요 수 
        Map<Long, Long> likeCounts = likeRepository.countLikesByCommentIds(commentIds);
        
        // 내가 좋아요 눌렀는지 여부
        Map<Long, Boolean> isLikedMap = loginUser != null
                ? likeRepository.getIsLikedMapByCommentIds(commentIds, loginUser)
                : Collections.emptyMap();
        
        // 각 댓글을 DTO 변환 
        return comments.stream()
                .map(comment -> commentMapper.toDto(
                        comment,
                        likeCounts.getOrDefault(comment.getId(), 0L),
                        isLikedMap.getOrDefault(comment.getId(), false),
                        loginUser != null && comment.getUser().getUserId().equals(loginUser.getUserId()))
                )
                .toList();
    }

    /**
     * 댓글 작성
     * - 리뷰가 유효하고 삭제되지 않았는지 확인 후 댓글을 저장
     */
    @Transactional
    public ReviewCommentCreateResponse createComment(Long reviewId, Long userId, ReviewCommentCreateRequest request) {
        // 유저 및 리뷰 유효성 확인
        UserInfo loginUser = getUserInfo(userId);
        Review review = getReview(reviewId);

        // 엔티티 변환 및 저장
        ReviewComment comment = commentMapper.toEntity(request, loginUser, review);
        commentRepository.save(comment);

        // 작성시 초기 좋아요 수는 0, 본인 작성 true
        return commentMapper.toDto(comment, 0L, false, true);
    }

    /**
     * 댓글 수정
     * - 본인이 작성한 댓글만 수정 가능하며, 좋아요 정보 포함한 결과를 반환
     */
    @Transactional
    public ReviewCommentCreateResponse updateComment(Long commentId, Long userId, ReviewCommentUpdateRequest request) {
        // 유저 확인 및 본인 댓글 검증
        UserInfo loginUser = getUserInfo(userId);
        ReviewComment comment = getOwnCommentOrThrow(commentId, loginUser.getUserId());

        // 내용 수정 후 저장
        comment.updateContent(request.getContent());
        commentRepository.save(comment);

        // 좋아요 수 및 상태 재계산
        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);
        boolean isLiked = likeRepository.findByReviewCommentIdAndUser(commentId, loginUser)
                .map(ReviewCommentLike::isLiked).orElse(false);

        return commentMapper.toDto(comment, likeCount, isLiked, true);
    }

    /**
     * 댓글 삭제 (soft delete)
     * - 본인만 삭제 가능하며, deletedAt 필드를 통해 논리적 삭제 처리
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        // 댓글 유효성 검증
        ReviewComment comment = getOwnCommentOrThrow(commentId, userId);
        // 삭제
        comment.deleteReviewComment();
    }


    /**
     * 댓글 좋아요 등록/취소
     * - 기존 좋아요 기록이 없으면 새로 생성, 있으면 상태만 변경
     */
    @Transactional
    public ReviewCommentLikeResponse updateLikeStatus(Long commentId, boolean isLiked, Long userId) {
        // 유저 확인 및 댓글 유효성 검증
        UserInfo loginUser = getUserInfo(userId);
        ReviewComment comment = getOwnCommentOrThrow(commentId,userId);

        // 기존 좋아요가 있는지 확인
        ReviewCommentLike like = likeRepository.findByReviewCommentIdAndUser(commentId, loginUser)
                .orElseGet(() -> {
                    ReviewCommentLike newLike = ReviewCommentLike.of(comment, loginUser, isLiked);
                    likeRepository.save(newLike);
                    return newLike;
                });
        // 상태 변경
        like.updateLikeStatus(isLiked);

        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);

        // DTO 반환
        return ReviewCommentLikeResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(commentId)
                .userId(loginUser.getUserId())
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }



    /**
     * 댓글 좋아요 여부 + 메타 정보 조회
     */
    public CommentLikeStatusResponse getLikeStatus(Long commentId, Long userId) {
        UserInfo user = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        ReviewComment comment = getOwnCommentOrThrow(commentId,userId);

        boolean isLiked = likeRepository.findByReviewCommentIdAndUser(commentId, user)
                .map(ReviewCommentLike::isLiked)
                .orElse(false);

        return CommentLikeStatusResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(commentId)
                .isLiked(isLiked)
                .build();
    }



    /**
     * 댓글 좋아요 여부 단건 조회
     */
    public boolean isLiked(Long commentId, Long userId) {
        UserInfo loginUser = getUserInfo(userId);
        ReviewComment comment = getOwnCommentOrThrow(commentId,userId);

        return likeRepository.findByReviewCommentIdAndUser(comment.getId(), loginUser)
                .map(ReviewCommentLike::isLiked)
                .orElse(false);
    }


    /**
     * 댓글 좋아요 수 조회
     */
    public CommentLikeCountResponse commentLikeCount(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        ReviewComment comment = commentRepository.findActiveCommentWithAliveReview(commentId).orElseThrow(CommentNotFoundException::new);

        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);

        return CommentLikeCountResponse.builder()
                .commentId(commentId)
                .reviewId(comment.getReview().getId())
                .likeCount(likeCount)
                .build();
    }




    //============== 공통 메서드 ===============

    // 1. 댓글 유효성 검증
    private ReviewComment getOwnCommentOrThrow(Long commentId, Long userId) {
        ReviewComment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);  // 아예 등록된적 없는 댓글

        if (!comment.getUser().getUserId().equals(userId)) {throw new UnauthorizedAccessException();} //본인 댓글 아님
        if (comment.getDeletedAt() != null) {throw new CommentNotFoundException(); }  //삭제된 댓글

        return comment;
    }
    // 2. 리뷰 검증 및 가져오기
    private Review getReview(Long reviewId) {
        // 등록 된 적 없는 리뷰 요청인지 확인
        reviewRepository.findReviewById(reviewId).orElseThrow(NotFoundException::new);
        // 삭제되지 않은 리뷰인지 검증
        Review review = reviewRepository.findActiveById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return review;
    }

    // 3. 로그인한 유저 검증 및 가져오기
    private UserInfo getUserInfo(Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        return loginUser;
    }




}








