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


    // 삭제된 리뷰 제외 조회
    @Query("SELECT r FROM Review r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<Review> findActiveById(@Param("id") Long id);

    @Query("""
    SELECT r FROM Review r
    JOIN Property p ON r.propertyId = p.propertyId
    WHERE p.complex.id = :complexId
      AND r.deletedAt IS NULL
      AND p.deletedAt IS NULL
        """)
    Page<Review> findReviewsByComplexId(@Param("complexId") Long complexId, Pageable pageable);



    // 복합단지 ID로 리뷰 조회
    @Query("SELECT r FROM Review r " +
            "WHERE r.complex.id = :complexId AND r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    Page<Review> findByComplexId(@Param("complexId") Long complexId, Pageable pageable);

    // 매물 ID로 리뷰 조회
    @Query("SELECT r FROM Review r " +
            "WHERE r.propertyId = :propertyId AND r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    Page<Review> findByPropertyId(@Param("propertyId") Long propertyId, Pageable pageable);


    // 복합단지에 대한 리뷰 조회
    default Page<Review> findReviewsByComplexId(Long complexId, String sort, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return findReviewsByComplexId(complexId, pageable);
    }

    // 매물에 대한 리뷰 조회
    default Page<Review> findReviewsByPropertyId(Long propertyId, String sort, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return findByPropertyId(propertyId, pageable);
    }

    //별점 평균 조회 -- 매물 별 조회일경우
    @Query("""
    SELECT COALESCE(ROUND(AVG(r.rating), 2), 0)
    FROM Review r
    WHERE r.propertyId = :propertyId
      AND r.deletedAt IS NULL
""")
    BigDecimal calculateAverageRating(@Param("propertyId") Long propertyId);


    //별점 평균 조회 -- 단지별 조회일경우
    @Query("""
    SELECT COALESCE(ROUND(AVG(r.rating), 2), 0)
    FROM Review r
    JOIN Property p ON r.propertyId = p.propertyId
    WHERE p.complex.id = :complexId
      AND r.deletedAt IS NULL
      AND p.deletedAt IS NULL
""")
    BigDecimal calculateAverageRatingByComplex(@Param("complexId") Long complexId);





    // 특정 유저의 리뷰 조회
    @Query("SELECT r FROM Review r " +
            "WHERE r.user = :user AND r.deletedAt IS NULL " +
            "ORDER BY r.createdAt DESC")
    List<Review> findActiveByUser(@Param("user") UserInfo user);

}

