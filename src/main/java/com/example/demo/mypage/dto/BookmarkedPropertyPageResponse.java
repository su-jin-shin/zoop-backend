package com.example.demo.mypage.dto;

import com.example.demo.property.dto.PropertyListItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookmarkedPropertyPageResponse {

    private List<PropertyListItemDto> bookmarkedProperties;
    private int page;
    private int size;
    private boolean hasNext;
}
