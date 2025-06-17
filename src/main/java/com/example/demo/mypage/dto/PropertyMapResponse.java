package com.example.demo.mypage.dto;

import com.example.demo.property.dto.PropertyListItemDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "mapProperties는 생성자에서 복사되어 방어됨"
)
@Getter
public class PropertyMapResponse {

    private final List<PropertyListItemDto> mapProperties;
    private final BookmarkedPropertyPageResponse bookmarkedPageResponse;

    public PropertyMapResponse(List<PropertyListItemDto> mapProperties, BookmarkedPropertyPageResponse bookmarkedPageResponse) {
        this.mapProperties = mapProperties == null ? null : new ArrayList<>(mapProperties);
        this.bookmarkedPageResponse = bookmarkedPageResponse;
    }

    public List<PropertyListItemDto> getMapProperties() {
        return mapProperties == null ? null : new ArrayList<>(mapProperties);
    }

    @Builder
    public static PropertyMapResponse of(List<PropertyListItemDto> mapProperties, BookmarkedPropertyPageResponse bookmarkedPageResponse) {
        return new PropertyMapResponse(mapProperties, bookmarkedPageResponse);
    }
}


