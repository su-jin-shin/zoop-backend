package com.example.demo.property.controller;

import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.common.response.ResponseResult;
import com.example.demo.property.domain.Property;
import com.example.demo.property.dto.*;
import com.example.demo.property.service.PropertyService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.common.response.SuccessMessage.CREATED_SUCCESSFULLY;
import static com.example.demo.common.response.SuccessMessage.GET_SUCCESS;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;

    //매물 상세 조회 (기본 정보) API
    @GetMapping("/{propertyId}/basic_info")
    public ResponseEntity<?> getBasicInfo(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {

        if (loginUser == null) {
            throw new UserNotFoundException(); // 또는 InvalidRequestException
        }
        Long userId =  Long.valueOf(loginUser.getUsername());
        PropertyBasicInfoResponseDto dto = propertyService.getPropertyBasicInfo(propertyId, userId);
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        dto
                )
        );
    }




    //매물 상세 조회 (상세설명) API
    @GetMapping("/{propertyId}/description")
    public ResponseEntity<?> getDescription(@PathVariable Long propertyId,@AuthenticationPrincipal LoginUser loginUser){
        if (loginUser == null) {
            throw new UserNotFoundException(); // 또는 InvalidRequestException
        }

        PropertyDescriptionResponseDto propertyDescriptionResponseDto = propertyService.getPropertyDescription(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyDescriptionResponseDto
                )
        );
    }

    //매물 상세조회 (시설정보) API
    @GetMapping("/{propertyId}/facilities")
    public ResponseEntity<?> getFacilities(@PathVariable Long propertyId, @AuthenticationPrincipal LoginUser loginUser){

        if(loginUser == null){
            throw new UserNotFoundException();
        }

        PropertyFacilitiesResponseDto propertyFacilitiesResponseDto = propertyService.getPropertyFacilities(propertyId);


        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyFacilitiesResponseDto
                )
        );
    }

    //매물 상세조회 (위치정보) API
    @GetMapping("/{propertyId}/location")
    public ResponseEntity<?> getLocation(@PathVariable Long propertyId, @AuthenticationPrincipal LoginUser loginUser){

        if(loginUser == null){
            throw new UserNotFoundException();
        }


        PropertyLocationResponseDto propertyLocationResponseDto = propertyService.getPropertyLocation(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyLocationResponseDto
                )
        );
    }

    //매물 상세조회 (거래정보) API
    @GetMapping("/{propertyId}/transaction")
    public ResponseEntity<?> getTransaction(@PathVariable Long propertyId, @AuthenticationPrincipal LoginUser loginUser){

        if(loginUser == null){
            throw new UserNotFoundException();
        }
        PropertyTransactionResponseDto propertyTransactionResponseDto = propertyService.getPropertyTransaction(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyTransactionResponseDto
                )
        );
    }

    //매물 상세조회 (중개정보) API
    @GetMapping("/{propertyId}/agent")
    public ResponseEntity<?> getAgent(@PathVariable Long propertyId, @AuthenticationPrincipal LoginUser loginUser){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        PropertyAgentResponseDto propertyAgentResponseDto = propertyService.getPropertyAgent(propertyId);


        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyAgentResponseDto
                )
        );
    }

    //매물 상세조회 (중개보수 및 세금정보) API
    @GetMapping("/{propertyId}/broker_fee")
    public ResponseEntity<?> getBroker(@PathVariable Long propertyId,@AuthenticationPrincipal LoginUser loginUser){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        PropertyBrokerFeeResponseDto propertyBrokerFeeResponseDto = propertyService.getBrokerFee(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyBrokerFeeResponseDto
                )
        );
    }

    //매물 상세조회 (매물 정보) API
    @GetMapping("/{propertyId}/property_info")
    public ResponseEntity<?> getPropertyInfo(@PathVariable Long propertyId,@AuthenticationPrincipal LoginUser loginUser){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        PropertyPropertyInfoResponseDto propertyPropertyInfoResponseDto = propertyService.getPropertyInfo(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyPropertyInfoResponseDto
                )
        );
    }

    //공인중개사 연락처 조회 (매물 상세페이지) API
    @GetMapping("/{propertyId}/agent_number")
    public ResponseEntity<?> getAgentNumber(@PathVariable Long propertyId, @AuthenticationPrincipal LoginUser loginUser){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        PropertyAgentNumberResponseDto propertyAgentNumberResponseDto = propertyService.getPropertyAgentNumber(propertyId);


        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyAgentNumberResponseDto
                )
        );
    }

    //매물 비교하기
    @GetMapping("/compare")
    public ResponseEntity<?> compareProperties(
            @RequestParam List<Long> propertyIds, @AuthenticationPrincipal LoginUser loginUser
    ){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        List<PropertyCompareResponseDto> result = propertyService.getCompareProperties(propertyIds);


        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        result
                )
        );

    }

    //매물 OG 정보 조회
    @GetMapping("/{propertyId}/property_og")
    public  ResponseEntity<?> getpropertyOg(@PathVariable Long propertyId,
                                                                @AuthenticationPrincipal LoginUser loginUser){

        PropertyOgResponseDto propertyOgResponseDto = propertyService.getPropertyOg(propertyId);
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        propertyOgResponseDto
                )
        );

    }

    // 부동산별 매물 보기 (부동산 정보)
    @GetMapping("/{propertyId}/realty")
    public ResponseEntity<?> getRealtyWithProperties (@PathVariable Long propertyId,
                                                                                    @AuthenticationPrincipal LoginUser loginUser){
        if(loginUser == null){
            throw new UserNotFoundException();
        }
        RealtyWithPropertiesResponseDto realtyWithPropertiesResponseDto = propertyService.getRealtyWithProperties(propertyId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        realtyWithPropertiesResponseDto
                )
        );
    }
}
