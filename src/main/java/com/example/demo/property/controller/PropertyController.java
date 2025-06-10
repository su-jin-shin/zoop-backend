package com.example.demo.property.controller;

import com.example.demo.property.dto.PropertyBasicInfoResponseDto;
import com.example.demo.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;

    //매물 상세 조회 (기본 정보) API
    @GetMapping("/{propertyId}/basic-info")
    public ResponseEntity<PropertyBasicInfoResponseDto> getBasicInfo(@PathVariable Long propertyId){
     PropertyBasicInfoResponseDto propertyBasicInfoResponseDto = propertyService.getPropertyBasicInfo(propertyId);

     if(propertyBasicInfoResponseDto == null) {
         return ResponseEntity.notFound().build();
     } //예외처리 추후 수정
         return ResponseEntity.ok(propertyBasicInfoResponseDto); // 정상응답 200
    }

}
