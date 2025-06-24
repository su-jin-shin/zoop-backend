package com.example.demo.review.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.review.domain.Review;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public interface ReviewRepository extends JpaRepository<Review, Long> {


    /**
     * 삭제되지 않은 리뷰 단건 조회
     * - soft delete 적용되어 deletedAt == null 조건 필요
     */
    @Query("SELECT r FROM Review r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<Review> findActiveById(@Param("id") Long id);

    // 삭제된것 포함 리뷰 단건 조회 (404 에러)
    @Query("SELECT r FROM Review r WHERE r.id = :id")
    Optional<Review> findReviewById(@Param("id") Long id);

    /**
     * 특정 단지에 속한 매물들의 리뷰 페이지 조회
     * - 매물과 리뷰 모두 삭제되지 않은 조건
     * - 페이징 필수
     */
    @Query("""
    SELECT r FROM Review r
    JOIN Property p ON r.propertyId = p.propertyId
    WHERE p.complex.id = :complexId
      AND r.deletedAt IS NULL
      AND p.deletedAt IS NULL
        """)
    Page<Review> findReviewsByComplexId(@Param("complexId") Long complexId, Pageable pageable);


    /**
     * (미사용 가능성 있음)
     * 복합 단지 ID로 리뷰 조회 - 기본 정렬(createdAt DESC)
     */
    @Query("SELECT r FROM Review r " +
            "WHERE r.complex.id = :complexId AND r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    Page<Review> findByComplexId(@Param("complexId") Long complexId, Pageable pageable);

    /**
     * 특정 매물 ID 기준 리뷰 조회
     * - soft delete 적용
     * - 최신순 정렬
     */
    @Query("SELECT r FROM Review r " +
            "WHERE r.propertyId = :propertyId AND r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    Page<Review> findByPropertyId(@Param("propertyId") Long propertyId, Pageable pageable);


    /**
     * [default method]
     * 단지 기준 리뷰 조회 + 정렬 옵션 처리
     */
    default Page<Review> findReviewsByComplexId(Long complexId, String sort, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return findReviewsByComplexId(complexId, pageable);
    }

    /**
     * [default method]
     * 매물 기준 리뷰 조회 + 정렬 옵션 처리
     */
    default Page<Review> findReviewsByPropertyId(Long propertyId, String sort, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return findByPropertyId(propertyId, pageable);
    }

    /**
     * 리뷰 별점 평균 조회 (매물 기준)
     * - 삭제되지 않은 리뷰만 대상
     * - null 방지를 위해 COALESCE 사용, 소수점 2자리 반올림
     */
    @Query("""
    SELECT COALESCE(ROUND(AVG(r.rating), 2), 0)
    FROM Review r
    WHERE r.propertyId = :propertyId
      AND r.deletedAt IS NULL
""")
    BigDecimal calculateAverageRating(@Param("propertyId") Long propertyId);


    /**
     * 리뷰 별점 평균 조회 (단지 기준)
     * - 리뷰, 매물 모두 삭제되지 않은 조건일 경우에만 별점 집계에 포함
     * - null 방지를 위해 COALESCE 사용, 소수점 2자리 반올림
     */
    @Query("""
    SELECT COALESCE(ROUND(AVG(r.rating), 2), 0)
    FROM Review r
    JOIN Property p ON r.propertyId = p.propertyId
    WHERE p.complex.id = :complexId
      AND r.deletedAt IS NULL
""")
    BigDecimal calculateAverageRatingByComplex(@Param("complexId") Long complexId);


}

