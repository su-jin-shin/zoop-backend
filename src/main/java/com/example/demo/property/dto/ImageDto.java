package com.example.demo.property.dto;

import com.example.demo.property.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ImageDto {

    private String imageUrl;
    private String imageType;
    private Integer imageOrder;
    private boolean isMain;



    public static ImageDto from(Image img) {
        return ImageDto.builder()
                .imageUrl(img.getImageUrl())
                .imageType(img.getImageType().name())
                .imageOrder(img.getImageOrder())
                .isMain(Boolean.TRUE.equals(img.getIsMain()))
                .build();
    }
}
