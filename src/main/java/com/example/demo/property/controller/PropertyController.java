package com.example.demo.property.controller;

import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.*;
import com.example.demo.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    @GetMapping("/{propertyId}/basic_info")
    public ResponseEntity<PropertyBasicInfoResponseDto> getBasicInfo(@PathVariable Long propertyId){
        PropertyBasicInfoResponseDto propertyBasicInfoResponseDto = propertyService.getPropertyBasicInfo(propertyId);

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

    //매물 상세조회 (거래정보) API
    @GetMapping("/{propertyId}/transaction")
    public ResponseEntity<PropertyTransactionResponseDto> getTransaction(@PathVariable Long propertyId){
        PropertyTransactionResponseDto propertyTransactionResponseDto = propertyService.getPropertyTransaction(propertyId);

        return ResponseEntity.ok(propertyTransactionResponseDto); //정상 응답 200
    }

    //매물 상세조회 (중개정보) API
    @GetMapping("/{propertyId}/agent")
    public ResponseEntity<PropertyAgentResponseDto> getAgent(@PathVariable Long propertyId){
        PropertyAgentResponseDto propertyAgentResponseDto = propertyService.getPropertyAgent(propertyId);

        return  ResponseEntity.ok(propertyAgentResponseDto); //정상 응답 200
    }

    //매물 상세조회 (중개보수 및 세금정보) API
    @GetMapping("/{propertyId}/broker_fee")
    public ResponseEntity<PropertyBrokerFeeResponseDto> getBroker(@PathVariable Long propertyId){
        PropertyBrokerFeeResponseDto propertyBrokerFeeResponseDto = propertyService.getBrokerFee(propertyId);

        return ResponseEntity.ok(propertyBrokerFeeResponseDto); //정상 응답 200
    }

    //매물 상세조회 (매물 정보) API
    @GetMapping("/{propertyId}/property_info")
    public ResponseEntity<PropertyPropertyInfoResponseDto> getPropertyInfo(@PathVariable Long propertyId){
        PropertyPropertyInfoResponseDto propertyPropertyInfoResponseDto = propertyService.getPropertyInfo(propertyId);

        return ResponseEntity.ok(propertyPropertyInfoResponseDto);
    }

    //공인중개사 연락처 조회 (매물 상세페이지) API
    @GetMapping("/{propertyId}/agent_number")
    public ResponseEntity<PropertyAgentNumberResponseDto> getAgentNumber(@PathVariable Long propertyId){
        PropertyAgentNumberResponseDto propertyAgentNumberResponseDto = propertyService.getPropertyAgentNumber(propertyId);

        return ResponseEntity.ok(propertyAgentNumberResponseDto);
    }




}
