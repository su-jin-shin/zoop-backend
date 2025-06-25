package com.example.demo.review.repository;

import com.example.demo.review.domain.ReviewSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
    /**
     * ReviewSummaryRepository
     *
     * 목적: 리뷰에 대한 요약 정보(ReviewSummary) 관리
     * - 매물/단지 단위 요약 정보 저장
     */

    // 특정 매물 ID에 대한 리뷰 요약 목록 조회
    List<ReviewSummary> findByPropertyId(Long propertyId);

    //특정 단지 ID에 대한 리뷰 요약 목록 조회
    List<ReviewSummary> findByComplexId(Long complexId);

    //단지 ID에 대한 요약 정보가 존재하는지 여부 확인
    boolean existsByComplexId(Long complexId);


}
