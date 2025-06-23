package com.example.demo.review.repository;

/*
목적 : 댓글에 대한 좋아요 정보 관리
 주요 기능:
 * - 좋아요 여부 단건 조회
 * - 좋아요 수 단건 및 여러 건 집계
 * - 사용자 기준 좋아요 여부 조회
 * - Map 형태로 결과 변환 (서비스에서 활용 용이)
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

    /**
     * 특정 댓글에 대해 사용자가 좋아요를 눌렀는지 여부 조회
     *
     * @param commentId 댓글 ID
     * @param user 사용자
     * @return Optional<ReviewCommentLike>
     */
    Optional<ReviewCommentLike> findByReviewCommentIdAndUser(Long commentId, UserInfo user);

    /**
     * 특정 댓글의 총 좋아요 수 조회 (isLiked = true 기준)
     *
     * @param commentId 댓글 ID
     * @return 좋아요 개수
     */
    long countByReviewCommentIdAndIsLikedTrue(Long commentId);

    /**
     * 여러 댓글에 대해 좋아요 수를 댓글 ID별로 집계
     *
     * @param commentIds 댓글 ID 리스트
     * @return (commentId, count) 형태의 Object[] 리스트
     */
    @Query("SELECT rcl.reviewComment.id, COUNT(rcl) " +
            "FROM ReviewCommentLike rcl " +
            "WHERE rcl.reviewComment.id IN :commentIds AND rcl.isLiked = true " +
            "GROUP BY rcl.reviewComment.id")
    List<Object[]> countLikesGroupByCommentIds(@Param("commentIds") List<Long> commentIds);

    /**
     * 사용자 기준으로 여러 댓글에 대해 좋아요 여부 조회
     *
     * @param commentIds 댓글 ID 리스트
     * @param user 사용자 정보
     * @return (commentId, isLiked) 형태의 Object[] 리스트
     */
    @Query("SELECT rcl.reviewComment.id, rcl.isLiked " +
            "FROM ReviewCommentLike rcl " +
            "WHERE rcl.reviewComment.id IN :commentIds AND rcl.user = :user")
    List<Object[]> findIsLikedByCommentIds(@Param("commentIds") List<Long> commentIds,
                                           @Param("user") UserInfo user);

    /**
     * [Default Method]
     * 여러 댓글에 대한 좋아요 수를 Map 형태로 반환
     *
     * @param commentIds 댓글 ID 리스트
     * @return Map<commentId, 좋아요 수>
     */
    default Map<Long, Long> countLikesByCommentIds(List<Long> commentIds) {
        return countLikesGroupByCommentIds(commentIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // commentId
                        row -> (Long) row[1]  // count
                ));
    }

    /**
     * [Default Method]
     * 사용자 기준 여러 댓글의 좋아요 여부를 Map 형태로 반환
     *
     * @param commentIds 댓글 ID 리스트
     * @param user 사용자
     * @return Map<commentId, isLiked>
     */
    default Map<Long, Boolean> getIsLikedMapByCommentIds(List<Long> commentIds, UserInfo user) {
        return findIsLikedByCommentIds(commentIds, user).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],    // commentId
                        row -> (Boolean) row[1]  // isLiked
                ));
    }
}