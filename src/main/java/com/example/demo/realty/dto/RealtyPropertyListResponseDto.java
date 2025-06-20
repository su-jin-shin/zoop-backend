// DTO: RealtyPropertyListResponseDto.java
package com.example.demo.realty.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.dto.ImageDto;
import com.example.demo.property.util.PropertyDtoConverter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.property.util.PropertyDtoConverter.convertImages;
import static com.example.demo.property.util.PropertyDtoConverter.convertSummary;
@SuppressFBWarnings({
        "EI_EXPOSE_REP", "EI_EXPOSE_REP2"
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtyPropertyListResponseDto {
    private List<PropertyListItemDto> properties;
    private int page;
    private int size;
    private boolean hasNext;

    public static RealtyPropertyListResponseDto of(Page<Property> propertyPage, List<PropertySummary> summaries, List<List<Image>> imageLists) {
        List<PropertyListItemDto> propertyDtos = new ArrayList<>();
        int order = 1 + (propertyPage.getNumber() * propertyPage.getSize());

        for (int i = 0; i < propertyPage.getContent().size(); i++) {
            Property property = propertyPage.getContent().get(i);

            List<Image> images = i < imageLists.size() ? imageLists.get(i) : Collections.emptyList();

            PropertyListItemDto dto = PropertyListItemDto.of(
                    property,

                    images,
                    false,
                    order++
            );
            propertyDtos.add(dto);
        }

        return RealtyPropertyListResponseDto.builder()
                .properties(propertyDtos)
                .page(propertyPage.getNumber())
                .size(propertyPage.getSize())
                .hasNext(propertyPage.hasNext())
                .build();
    }
}
