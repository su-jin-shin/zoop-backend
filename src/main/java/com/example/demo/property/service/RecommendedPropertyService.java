package com.example.demo.property.service;

import com.example.demo.property.dto.RecommendedPropertyDto;

import java.util.List;

public interface RecommendedPropertyService {

    // 추천 매물 insert
    void saveRecommendedPropertyForMessage(Long messageId, List<RecommendedPropertyDto> recommendedProperties);

}
