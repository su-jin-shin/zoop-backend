package com.example.demo.mypage.dto;

import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookmarkedPropertyPageResponse {

    private final List<PropertyListItemDto> bookmarkedProperties;
    private final int page;
    private final int size;
    private final boolean hasNext;

    public BookmarkedPropertyPageResponse(List<PropertyListItemDto> bookmarkedProperties, int page, int size, boolean hasNext) {
        this.bookmarkedProperties = bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }

    public List<PropertyListItemDto> getBookmarkedProperties() {
        return bookmarkedProperties == null ? null : new ArrayList<>(bookmarkedProperties);
    }
} 