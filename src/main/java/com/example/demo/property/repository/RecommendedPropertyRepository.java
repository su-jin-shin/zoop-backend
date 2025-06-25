package com.example.demo.property.repository;

import com.example.demo.property.domain.RecommendedProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedPropertyRepository extends JpaRepository<RecommendedProperty, Long> {

    List<RecommendedProperty> findByMessage_MessageIdIn(List<Long> messageIds);

}
