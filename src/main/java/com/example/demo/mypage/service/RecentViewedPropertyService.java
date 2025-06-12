package com.example.demo.mypage.service;

import com.example.demo.mypage.dto.RecentViewedPropertyResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RecentViewedPropertyService {

    void save(Long userId, Long propertyId);
    List<RecentViewedPropertyResponse> getRecentViewedList(Long userId);
}
