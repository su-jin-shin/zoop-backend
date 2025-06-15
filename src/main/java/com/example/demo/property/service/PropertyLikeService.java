package com.example.demo.property.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.property.dto.BookmarkedPropertyRequestDto;
import com.example.demo.property.dto.BookmarkedPropertyResponseDto;

public interface PropertyLikeService {
    BookmarkedPropertyResponseDto likeProperty(Long userId, Long propertyId);
}
