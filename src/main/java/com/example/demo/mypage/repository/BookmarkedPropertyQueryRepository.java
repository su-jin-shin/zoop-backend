package com.example.demo.mypage.repository;

import com.example.demo.mypage.domain.BookmarkedProperty;

import java.util.List;

public interface BookmarkedPropertyQueryRepository {
    List<BookmarkedProperty> findAllWithPropertyByUserId(Long userId);
}
