package com.example.demo.mypage.service;


import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.dto.PropertyMapResponse;


import com.example.demo.realty.dto.PropertyListItemDto;


import java.util.List;

public interface RecentViewedPropertyService {

    void save(Long userId, Long propertyId);
    List<PropertyListItemDto> getRecentViewedList(Long userId);

    PropertyMapResponse getRecentViewedPropertiesWithMap(Long userId);

    List<PropertyExcelDto> getRecentViewedPropertiesForExcel(Long userId);
}
