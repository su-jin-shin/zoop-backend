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

    private String imageUrl; //이미지 경로
    private String imageType; //이미지 타입
    private Integer imageOrder; //정렬 순서
    private boolean isMain; //썸네일 여부



    public static ImageDto from(Image img) {
        return ImageDto.builder()
                .imageUrl(img.getImageUrl())
                .imageType(img.getImageType().name())
                .imageOrder(img.getImageOrder())
                .isMain(Boolean.TRUE.equals(img.getIsMain()))
                .build();
    }
}
