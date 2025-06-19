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

    // 삭제되지 않은 리뷰에 대해서만 댓글 조회 가능
//    @Query("SELECT c FROM ReviewComment c " +
//            "JOIN FETCH c.review r " +
//            "WHERE c.id = :commentId AND c.user.userId = :userId AND r.deletedAt IS NULL")
//    Optional<ReviewComment> findActiveCommentWithAliveReview(Long commentId);
    @Query("SELECT c FROM ReviewComment c JOIN FETCH c.review WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<ReviewComment> findActiveCommentWithAliveReview(@Param("id") Long id);


    // 댓글 목록 조회
    @Query("SELECT rc FROM ReviewComment rc WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL")
    List<ReviewComment> findByReviewId(Long reviewId);

    // 댓글에 개수 조회 == 중복 가능성 존재. 후에 변경 또는 삭제 예정
    @Query("SELECT COUNT(rc) FROM ReviewComment rc WHERE rc.review.id = :reviewId AND rc.deletedAt IS NULL")
    long commentCount(Long reviewId);

    // 리뷰 ID 리스트에 대해 댓글 수 조회
    @Query("SELECT rc.review.id, COUNT(rc) FROM ReviewComment rc " +
            "WHERE rc.deletedAt IS NULL AND rc.review.id IN :reviewIds " +
            "GROUP BY rc.review.id")
    List<Object[]> countCommentsByReviewIds(List<Long> reviewIds);

    // 댓글 수 조회 - default 메서드로 Map 반환
    default Map<Long, Long> countCommentsMap(List<Long> reviewIds) {
        List<Object[]> rows = countCommentsByReviewIds(reviewIds);
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((Long) row[0], (Long) row[1]);
        }
        return map;
    }
    //내 댓글 조회
    @Query("SELECT c FROM ReviewComment c " +
            "WHERE c.user = :user AND c.deletedAt IS NULL " +
            "ORDER BY c.createdAt DESC")
    List<ReviewComment> findActiveByUser(@Param("user") UserInfo user);

}

