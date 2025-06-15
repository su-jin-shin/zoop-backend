package com.example.demo.property.controller;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.dto.LoginUser;
import com.example.demo.property.dto.BookmarkedPropertyRequestDto;
import com.example.demo.property.dto.BookmarkedPropertyResponseDto;
import com.example.demo.property.service.PropertyLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyLikeController {

    private final PropertyLikeService propertyLikeService;

    @PostMapping("/{propertyId}/likes")
    public ResponseEntity<BookmarkedPropertyResponseDto> likeProperty(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = Long.valueOf(loginUser.getUsername()); // 또는 loginUser.getUserInfo().getUserId()

        BookmarkedPropertyResponseDto response = propertyLikeService.likeProperty(userId, propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
