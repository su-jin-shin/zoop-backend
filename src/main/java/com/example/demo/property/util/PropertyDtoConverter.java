package com.example.demo.property.util;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.PropertySummary;
import com.example.demo.property.dto.ImageDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyDtoConverter {
    //summary 데이터 타입  String -> 리스트 변환 메서드
    public static List<String> convertSummary(PropertySummary summaryEntity){
        if(summaryEntity == null || summaryEntity.getSummary() == null || summaryEntity.getSummary().isBlank()){
            return Collections.emptyList();
        }
        return Arrays.stream(summaryEntity.getSummary().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }


    //images 변환 메서드 (List<Image> -> List<ImageDto> 변환)
    public static List<ImageDto> convertImages(List<Image> images) {
        if(images == null || images.isEmpty()){
            return Collections.emptyList();
        }
        return images.stream()
                .map(ImageDto::from)
                .collect(Collectors.toList());
    }
}
