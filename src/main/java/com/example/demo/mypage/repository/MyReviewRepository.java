package com.example.demo.mypage.repository;

import com.example.demo.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId);

    @Query("SELECT COUNT(rc) FROM ReviewComment rc WHERE rc.review.id = :reviewId")
    int countByReviewId(Long reviewId);
}
