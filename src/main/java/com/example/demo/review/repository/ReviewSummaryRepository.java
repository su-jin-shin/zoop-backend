package com.example.demo.review.repository;

import com.example.demo.review.domain.ReviewSummary;
import com.example.demo.review.domain.SummaryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {

    List<ReviewSummary> findByPropertyId(Long propertyId);

    List<ReviewSummary> findByComplexId(Long complexId);

    boolean existsByComplexId(Long complexId);


}
