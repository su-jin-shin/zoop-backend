package com.example.demo.realty.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.realty.dto.RealtyAgentNumberResponseDto;

import com.example.demo.realty.dto.RealtyPropertyListResponseDto;
import com.example.demo.realty.service.RealtyService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Pageable;

import static com.example.demo.common.response.SuccessMessage.GET_SUCCESS;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
@RestController
@RequiredArgsConstructor
@RequestMapping("/realties")
public class RealtyController {
    private final RealtyService realtyService;




    //공인중개사 정보보기 (부동산)
    @GetMapping("/{realtyId}/agent_number")
    public ResponseEntity<RealtyAgentNumberResponseDto> getRealtyAgentContact(@PathVariable Long realtyId,@AuthenticationPrincipal LoginUser loginUser){
        if (loginUser == null) {
            throw new UserNotFoundException(); // 또는 InvalidRequestException
        }
        return ResponseEntity.ok(realtyService.getRealtyAgentNumber(realtyId));
    }

    @GetMapping("/{realtyId}/properties")
    public ResponseEntity<?> getPropertiesByRealty(
            @PathVariable Long realtyId,
            @RequestParam(required = false) String tradeTypeName, //  거래유형 필터 (월세, 전세, 매매)
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal LoginUser loginUser
    ) {

        if(loginUser == null){
            throw new UserNotFoundException();
        }
        Long userId = (loginUser != null) ?loginUser.getUserId() : null;

        RealtyPropertyListResponseDto response = realtyService.getPropertiesByRealty(realtyId, tradeTypeName, userId,pageable);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        response
                )
        );
    }



}
