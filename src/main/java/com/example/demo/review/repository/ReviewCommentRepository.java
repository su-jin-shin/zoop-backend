package com.example.demo.review.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.ReviewComment;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {


    /**
     * 댓글 ID를 기준으로 삭제되지 않은 댓글 + 삭제되지 않은 리뷰에 대한 댓글 단건 조회
     * - JOIN FETCH를 사용해 댓글과 연결된 리뷰를 함께 조회 (N+1 문제 방지)
     * - 댓글, 리뷰 모두 삭제되지 않아야 조회 가능
     */
    @Query("SELECT c FROM ReviewComment c " +
            "JOIN FETCH c.review r " +
            "JOIN c.user u " +
            "WHERE c.id = :commentId AND r.deletedAt IS NULL AND c.deletedAt IS NULL AND u.deletedAt IS NULL " +
            "ORDER BY c.createdAt DESC")
    Optional<ReviewComment> findActiveCommentWithAliveReview(@Param("commentId") Long commentId);


    /**
     * 특정 리뷰에 달린 댓글 목록 조회
     * - 삭제되지 않은 댓글만 조회
     * - 작성일 기준 최신 순 정렬
     */
    @Query("SELECT rc FROM ReviewComment rc " +
            "JOIN rc.user u " +
            "WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL AND u.deletedAt IS NULL " +
            "ORDER BY rc.createdAt DESC")
    List<ReviewComment> findByReviewId(Long reviewId);



    /**
     * 여러 리뷰 ID에 대해 각각의 댓글 수를 집계하여 반환
     * - 삭제된 댓글 제외
     */
    @Query("SELECT rc.review.id, COUNT(rc) FROM ReviewComment rc " +
            "JOIN rc.user u " +
            "WHERE rc.deletedAt IS NULL AND u.deletedAt IS NULL AND rc.review.id IN :reviewIds " +
            "GROUP BY rc.review.id")
    List<Object[]> countCommentsByReviewIds(List<Long> reviewIds);

    /**
     * [Default Method]
     * 리뷰 ID별 댓글 수를 Map 형태로 변환하여 반환
     * - 내부적으로 countCommentsByReviewIds() 사용
     */
    default Map<Long, Long> countCommentsMap(List<Long> reviewIds) {
        List<Object[]> rows = countCommentsByReviewIds(reviewIds);
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Long) row[1]);
        }
        return map;
    }

    /**
     * 특정 리뷰에 달린 댓글 개수 조회
     * - 삭제된 댓글 제외
     */
    @Query("SELECT COUNT(rc) FROM ReviewComment rc " +
            "JOIN rc.user u " +
            "WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL AND u.deletedAt IS NULL")
    long commentCount(Long reviewId);

}

