package com.example.demo.mypage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileImageService {

    String uploadAndSave(Long userId, MultipartFile file) throws IOException;
    String resetToDefaultImage(Long userId);
}
