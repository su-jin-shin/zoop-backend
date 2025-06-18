package com.example.demo.mypage.dto;


import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MyPropertyPageResponse {

    private final List<PropertyListItemDto> myProperties;
    private final int page;
    private final int size;
    private final boolean hasNext;

    public MyPropertyPageResponse(List<PropertyListItemDto> myProperties, int page, int size, boolean hasNext) {
        this.myProperties = myProperties == null ? null : new ArrayList<>(myProperties);
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }

    public List<PropertyListItemDto> getMyProperties() {
        return myProperties == null ? null : new ArrayList<>(myProperties);
    }
}