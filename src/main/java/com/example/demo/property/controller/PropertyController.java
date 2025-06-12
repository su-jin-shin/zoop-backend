package com.example.demo.property.controller;

import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.PropertyBasicInfoResponseDto;
import com.example.demo.property.dto.PropertyDescriptionResponseDto;
import com.example.demo.property.dto.PropertyFacilitiesResponseDto;
import com.example.demo.property.dto.PropertyLocationResponseDto;
import com.example.demo.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
     } //예외처리 추후 수정 (전역)
         return ResponseEntity.ok(propertyBasicInfoResponseDto); // 정상응답 200
    }


    //매물 상세 조회 (상세설명) API
    @GetMapping("/{propertyId}/description")
    public ResponseEntity<PropertyDescriptionResponseDto> getDescription(@PathVariable Long propertyId){
        PropertyDescriptionResponseDto propertyDescriptionResponseDto = propertyService.getPropertyDescription(propertyId);


        return ResponseEntity.ok(propertyDescriptionResponseDto); //정상응답 200
    }

    //매물 상세조회 (시설정보) API
    @GetMapping("/{propertyId}/facilities")
    public ResponseEntity<PropertyFacilitiesResponseDto> getFacilities(@PathVariable Long propertyId){
        PropertyFacilitiesResponseDto propertyFacilitiesResponseDto = propertyService.getPropertyFacilities(propertyId);
        

        return ResponseEntity.ok(propertyFacilitiesResponseDto); //정상 응답 200
    }

    //매물 상세조회 (위치정보) API
    @GetMapping("/{propertyId}/location")
    public ResponseEntity<PropertyLocationResponseDto> getLocation(@PathVariable Long propertyId){
        PropertyLocationResponseDto propertyLocationResponseDto = propertyService.getPropertyLocation(propertyId);

        return ResponseEntity.ok(propertyLocationResponseDto); //정상 응답 200
    }

}
