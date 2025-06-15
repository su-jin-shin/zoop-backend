package com.example.demo.realty.controller;

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

    @GetMapping("/{realtyId}/properties")
    public ResponseEntity<RealtyWithPropertiesResponseDto> getRealtyWithProperties(@PathVariable Long realtyId, @RequestParam(required = false) String dealType){
        RealtyWithPropertiesResponseDto response = realtyService.getRealtyWithProperties(realtyId,dealType);
        return ResponseEntity.ok(response);
    }
}
