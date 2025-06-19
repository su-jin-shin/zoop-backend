package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.*;
import com.example.demo.property.repository.PropertyRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewCommentService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;
    private final ReviewCommentLikeRepository likeRepository;
    private final ReviewCommentMapper commentMapper;
    private final PropertyRepository propertyRepository;
    private final UserInfoRepository userInfoRepository;

    /**
    리뷰에 달린 댓글 목록 조회 == 단일 리뷰 조회와 화면 동일
     **/

    public List<ReviewCommentCreateResponse> getComments(Long reviewId, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리

        //삭제되지 않은 리뷰인지 확인
        reviewRepository.findActiveById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);


        List<ReviewComment> comments = commentRepository.findByReviewId(reviewId);
        List<Long> commentIds = comments.stream()
                .map(ReviewComment::getId)
                .toList();

        Map<Long, Long> likeCounts = likeRepository.countLikesByCommentIds(commentIds);
        Map<Long, Boolean> isLikedMap = loginUser != null
                ? likeRepository.getIsLikedMapByCommentIds(commentIds, loginUser)
                : Collections.emptyMap();

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
    댓글 생성
     **/
    @Transactional
    public ReviewCommentCreateResponse createComment(Long reviewId, Long userId, ReviewCommentCreateRequest request) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);  // 유저가 없으면 예외 처리

        //리뷰 삭제 여부 확인
        Review review = reviewRepository.findActiveById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);


        ReviewComment comment = commentMapper.toEntity(request, loginUser, review);
        commentRepository.save(comment);

        return commentMapper.toDto(comment, 0L, false, true);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public ReviewCommentCreateResponse updateComment(Long commentId, Long userId, ReviewCommentUpdateRequest request) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        ReviewComment comment = getOwnCommentOrThrow(commentId, loginUser.getUserId());

        comment.updateContent(request.getContent());
        commentRepository.save(comment);

        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);
        boolean isLiked = likeRepository.findByReviewCommentIdAndUser(commentId, loginUser)
                .map(ReviewCommentLike::isLiked).orElse(false);

        return commentMapper.toDto(comment, likeCount, isLiked, true);
    }

    /**
     * 댓글 삭제 (soft delete)
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        ReviewComment comment = getOwnCommentOrThrow(commentId, userId);
        comment.deleteReviewComment();
    }


    /**
     * 댓글 좋아요 등록/해제
     */
    @Transactional
    public ReviewCommentLikeResponse updateLikeStatus(Long commentId, boolean isLiked, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        ReviewComment comment = commentRepository.findActiveCommentWithAliveReview(commentId)
                .orElseThrow(CommentNotFoundException::new);

        ReviewCommentLike like = likeRepository.findByReviewCommentIdAndUser(commentId, loginUser)
                .orElseGet(() -> {
                    ReviewCommentLike newLike = ReviewCommentLike.of(comment, loginUser, isLiked);
                    likeRepository.save(newLike); // 새로 생성 시에만 저장
                    return newLike;
                });

        like.updateLikeStatus(isLiked); // 항상 상태 업데이트

        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);

        return ReviewCommentLikeResponse.builder()
                .reviewId(comment.getReview().getId())
                .commentId(commentId)
                .userId(loginUser.getUserId())
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }


    //좋아요 여부 확인
    public CommentLikeStatusResponse getLikeStatus(Long commentId, Long userId) {
        UserInfo user = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        ReviewComment comment = commentRepository.findActiveCommentWithAliveReview(commentId)
                .orElseThrow(CommentNotFoundException::new);

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
     * 댓글 좋아요 여부 조회 (단건)
     */
    public boolean isLiked(Long commentId, Long userId) {
        UserInfo loginUser = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        ReviewComment comment = commentRepository.findActiveCommentWithAliveReview(commentId)
                .orElseThrow(CommentNotFoundException::new);

        return likeRepository.findByReviewCommentIdAndUser(comment.getId(), loginUser)
                .map(ReviewCommentLike::isLiked)
                .orElse(false);
    }

    /**
     * 댓글 개수 조회  --> 전체 리뷰 조회와 중복된 로직. 후에 변경 또는 삭제 예정
     */
    public Long getCommentCount(Long reviewId) {
        return commentRepository.commentCount(reviewId);
    }
    /**
     * 댓글 좋아요 수 조회
     */
    public CommentLikeCountResponse commentLikeCount(Long commentId) {
        ReviewComment comment = commentRepository.findActiveCommentWithAliveReview(commentId)
                .orElseThrow(CommentNotFoundException::new);

        long likeCount = likeRepository.countByReviewCommentIdAndIsLikedTrue(commentId);

        return CommentLikeCountResponse.builder()
                .commentId(commentId)
                .reviewId(comment.getReview().getId())
                .likeCount(likeCount)
                .build();
    }






    /**
     * 댓글 작성자 본인 여부 확인 및 삭제 여부 확인하는 공통 메서드
     */
    private ReviewComment getOwnCommentOrThrow(Long commentId, Long userId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundException::new);

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        if (comment.getDeletedAt() != null) {
            throw new CommentNotFoundException(); // 삭제된 댓글
        }


        return comment;
    }



    // 내 댓글 조회
    public List<ReviewCommentCreateResponse> getMyComments(Long userId) {
        UserInfo user = userInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        List<ReviewComment> comments = commentRepository.findActiveByUser(user);

        List<Long> commentIds = comments.stream()
                .map(ReviewComment::getId)
                .toList();

        Map<Long, Long> likeCounts = likeRepository.countLikesByCommentIds(commentIds);
        Map<Long, Boolean> isLikedMap = likeRepository.getIsLikedMapByCommentIds(commentIds, user);

        return comments.stream()
                .map(comment -> commentMapper.toDto(
                        comment,
                        likeCounts.getOrDefault(comment.getId(), 0L),
                        isLikedMap.getOrDefault(comment.getId(), false),
                        true // 내 댓글이므로 isMine = true
                )).toList();
    }


}








