package com.example.demo.review.repository;

/*
목적 :리뷰 본문, 별점 등 처리 전담
메서드 : 리뷰 목록 조회, 정렬, 사용자 리뷰 조회 등
*/

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    // 1. 특정 단지 기반 페이징 + 정렬
    @Query("SELECT r FROM Review r WHERE r.complex.Id = :complexId AND r.deletedAt IS NULL")
    Page<Review> findByComplexId(@Param("complexId")Long complexId, Pageable pageable);

    // 2. 특정 매물 기반 페이징 + 정렬
    @Query("SELECT r FROM Review r WHERE r.propertyId = :propertyId AND r.deletedAt IS NULL")
    Page<Review> findByPropertyId(@Param("propertyId") Long propertyId, Pageable pageable);

    // 3. 내 리뷰 (마이페이지 or 필터용)
    @Query("SELECT r FROM Review r WHERE r.user = :user AND r.deletedAt IS NULL")
    Page<Review> findByUser(@Param("user") UserInfo user, Pageable pageable);

    // 4. 단일 리뷰 조회
    Optional<Review> findById(Long reviewId);


}
