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
public class ImageUrlOnlyDto {
    private String imageUrl; //이미지 경로

    public static ImageUrlOnlyDto from(Image image){
        return new ImageUrlOnlyDto(image.getImageUrl());
    }
}
