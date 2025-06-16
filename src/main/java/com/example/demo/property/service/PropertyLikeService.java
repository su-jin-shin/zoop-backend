package com.example.demo.property.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.property.dto.BookmarkedPropertyRequestDto;
import com.example.demo.property.dto.BookmarkedPropertyResponseDto;

public interface PropertyLikeService {

    //매물 찜 등록
    BookmarkedPropertyResponseDto likeProperty(Long userId, Long propertyId);

    //매물 찜 취소
    BookmarkedPropertyResponseDto unlikeProperty(Long userId, Long propertyId);
}
