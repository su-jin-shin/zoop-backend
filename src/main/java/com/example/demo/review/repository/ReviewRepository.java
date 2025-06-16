package com.example.demo.review.repository;

/*
목적 :리뷰 본문, 별점 등 처리 전담
메서드 : 리뷰 목록 조회, 정렬, 사용자 리뷰 조회 등
*/

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.InvalidRequestException;
import com.example.demo.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "WHERE r.complex.id = :complexId AND r.deletedAt IS NULL")
    Page<Review> findByComplexId(@Param("complexId") Long complexId, Pageable pageable);

    @Query("SELECT r FROM Review r " +
            "WHERE r.propertyId = :propertyId AND r.deletedAt IS NULL")
    Page<Review> findByPropertyId(@Param("propertyId") Long propertyId, Pageable pageable);

    @Query("SELECT r FROM Review r " +
            "WHERE r.user = :user AND r.deletedAt IS NULL")
    Page<Review> findByUser(@Param("user") UserInfo user, Pageable pageable);

    // 공통 조회 메서드 추상화
    default Page<Review> findPagedReviews(Long complexId, Long propertyId, String sort, boolean isMine,
                                          LoginUser loginUser, int page, int size) {
        Sort sortOption = switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "like" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        Pageable pageable = PageRequest.of(page, size, sortOption);

        if (isMine) {
            return findByUser(loginUser.getUserInfo(), pageable);
        } else if (complexId != null) {
            return findByComplexId(complexId, pageable);
        } else if (propertyId != null) {
            return findByPropertyId(propertyId, pageable);
        } else {
            throw new InvalidRequestException();
        }
    }
}
