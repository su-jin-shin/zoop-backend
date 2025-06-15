package com.example.demo.property.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedPropertyResponseDto {
    private Long propertyId;
    private Boolean isBookmarked;
}
