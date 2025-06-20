package com.example.demo.mypage.repository;

import com.example.demo.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface MyReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId);

    @Query("SELECT COUNT(rc) FROM ReviewComment rc WHERE rc.review.id = :reviewId")
    int countByReviewId(Long reviewId);

    @Query("""
    SELECT COUNT(rl) 
    FROM ReviewLike rl 
    WHERE rl.review.id = :reviewId 
      AND rl.isLiked = true
""")
    long countByReviewIdAndIsLikedTrue(@Param("reviewId") Long reviewId);

    @Query("""
    SELECT rl.review.id, rl.isLiked 
    FROM ReviewLike rl 
    WHERE rl.review.id IN :reviewIds 
      AND rl.user.userId = :userId
""")
    List<Object[]> findIsLikedByReviewIds(@Param("reviewIds") List<Long> reviewIds, @Param("userId") Long userId);

    default Map<Long, Boolean> getIsLikedMapByReviewIds(List<Long> reviewIds, Long userId) {
        return findIsLikedByReviewIds(reviewIds, userId).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Boolean) row[1]
                ));
    }
}