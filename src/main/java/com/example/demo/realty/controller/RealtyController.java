package com.example.demo.realty.controller;

import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;
import com.example.demo.realty.dto.RealtyWithPropertiesResponseDto;
import com.example.demo.realty.service.RealtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/realties")
public class RealtyController {
    private final RealtyService realtyService;

    //부동산 별 매물보기 상세페이지 API
    @GetMapping("/{realtyId}/properties")
    public ResponseEntity<RealtyWithPropertiesResponseDto> getRealtyWithProperties(@PathVariable Long realtyId, @RequestParam(required = false) String dealType){
        RealtyWithPropertiesResponseDto response = realtyService.getRealtyWithProperties(realtyId,dealType);
        return ResponseEntity.ok(response);
    }

    //공인중개사 정보보기 (부동산)
    @GetMapping("/{realtyId}/agent_number")
    public ResponseEntity<RealtyAgentNumberResponseDto> getRealtyAgentContact(@PathVariable Long realtyId){
        return ResponseEntity.ok(realtyService.getRealtyAgentNumber(realtyId));
    }

    //매물 찜 등록





}
