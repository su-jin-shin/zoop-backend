package com.example.demo.Filter.controller;


import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.Filter.service.FilterService;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.common.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.common.response.SuccessMessage.CREATED_SUCCESSFULLY;
import static com.example.demo.common.response.SuccessMessage.GET_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final FilterService filterService;

    // 필터 조건 등록시 키워드 필터 히스토리 등록
    @PostMapping("/keyword-filters")
    public ResponseEntity<?> saveKeywordFilter(@AuthenticationPrincipal LoginUser loginUser,
                                              @RequestBody FilterRequestDto filterRequestDto) {

        // 로그인한 유저의 userId 추출
        Long userId = Long.valueOf(loginUser.getUsername());

        filterService.saveKeywordFilter(userId, filterRequestDto);
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.CREATED,
                        CREATED_SUCCESSFULLY.getMessage(),
                        null
                )
        );
    }


    // 기존 키워드 필터 히스토리 수정
    @PutMapping("/{keywordFilterHistoryId}")
    public ResponseEntity<?> updateKeywordFilter(@AuthenticationPrincipal LoginUser loginUser,
                                                 @PathVariable("keywordFilterHistoryId") Long keywordFilterHistoryId,
                                                 @RequestBody FilterRequestDto updateRequestDto) {
        Long userId = Long.valueOf(loginUser.getUsername());

        filterService.modifyKeywordFilter(userId, keywordFilterHistoryId, updateRequestDto);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        null
                )
        );
    }

    // 기존 키워드 필터 히스트로리에서 삭제 ( 실제로는 isUsed = true -> false 처리)
    @PatchMapping("/{keywordFilterHistoryId}")
    public ResponseEntity<?> deactivateKeywordFilter(@AuthenticationPrincipal LoginUser loginUser,
                                                     @PathVariable("keywordFilterHistoryId") Long keywordFilterHistoryId){
        Long userId = Long.valueOf(loginUser.getUsername());

        filterService.deactivateKeywordFilter(userId, keywordFilterHistoryId);

        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        null
                )
        );
    }

    // 사용자 등록한 필터 조건 전체 목록 조회
    @GetMapping()
    public ResponseEntity<?> getAllKeywordFilter(@AuthenticationPrincipal LoginUser loginUser){
        Long userId = Long.valueOf(loginUser.getUsername());
        List<String> filterTitles = filterService.getAllFilterTitlesByUser(userId);
        return ResponseEntity.ok(
                ResponseResult.success(
                        HttpStatus.OK,
                        GET_SUCCESS.getMessage(),
                        filterTitles
                )
        );
    }

    // 로그인한 사용자의 필터 히스토리(키워드 목록) 중에서 하나를 선택하고 상세정보 조회
//    @GetMapping("/{keywordFilterHistoryId}")
//    public ResponseEntity<?> getKeywordFilterDetail(@AuthenticationPrincipal LoginUser loginUser,
//                                                    @PathVariable("keywordFilterHistoryId") Long keywordFilterHistoryId){
//        Long userId = Long.valueOf(loginUser.getUsername());
//        KeywordFilterHistoryResponseDto keywordFilterHistoryResponseDto = filterService.getKeywordFilterDetail(userId, keywordFilterHistoryId);
//        return ResponseEntity.ok(
//                ResponseResult.success(
//                        HttpStatus.OK,
//                        GET_SUCCESS.getMessage(),
//                        keywordFilterHistoryResponseDto
//                )
//        );
//    }

}

