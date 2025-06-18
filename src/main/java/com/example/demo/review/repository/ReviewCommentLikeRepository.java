package com.example.demo.review.repository;

/*
목적 : 댓글에 대한 좋아요 처리
메서드 : 좋아요 여부/개수 조회, 토글 등록/수정, 개수 조회 등
 */

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.domain.ReviewCommentLike;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ReviewCommentLikeRepository extends JpaRepository<ReviewCommentLike, Long> {




    // 좋아요 여부 조회
    Optional<ReviewCommentLike> findByReviewCommentIdAndUser(Long commentId, UserInfo user);


    // 좋아요 개수 조회
    long countByReviewCommentIdAndIsLikedTrue(Long commentId);

    //  여러 댓글 좋아요 수 조회
    @Query("SELECT rcl.reviewComment.id, COUNT(rcl) " +
            "FROM ReviewCommentLike rcl " +
            "WHERE rcl.reviewComment.id IN :commentIds AND rcl.isLiked = true " +
            "GROUP BY rcl.reviewComment.id")
    List<Object[]> countLikesGroupByCommentIds(@Param("commentIds") List<Long> commentIds);

    //  사용자 기준 여러 댓글 좋아요 여부 조회
    @Query("SELECT rcl.reviewComment.id, rcl.isLiked " +
            "FROM ReviewCommentLike rcl " +
            "WHERE rcl.reviewComment.id IN :commentIds AND rcl.user = :user")
    List<Object[]> findIsLikedByCommentIds(@Param("commentIds") List<Long> commentIds,
                                           @Param("user") UserInfo user);

    //  디폴트 변환: 좋아요 수 Map
    default Map<Long, Long> countLikesByCommentIds(List<Long> commentIds) {
        return countLikesGroupByCommentIds(commentIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    //  디폴트 변환: 좋아요 여부 Map
    default Map<Long, Boolean> getIsLikedMapByCommentIds(List<Long> commentIds, UserInfo user) {
        return findIsLikedByCommentIds(commentIds, user).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Boolean) row[1]
                ));
    }
}


