package com.example.demo.property.repository;

import com.example.demo.property.domain.PropertySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertySummaryRepository extends JpaRepository<PropertySummary,Long> {

    // 매물 ID로 매물 요약 정보 조회
    Optional<PropertySummary> findByProperty_PropertyId(Long propertyId);
    List<PropertySummary> findByProperty_PropertyIdIn(List<Long> propertyIds);
}
