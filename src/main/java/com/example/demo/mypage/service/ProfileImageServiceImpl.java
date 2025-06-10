package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import com.example.demo.mypage.service.ProfileImageService;
import com.example.demo.mypage.util.FileUploader;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final MypageUserInfoRepository userInfoRepository;
    private final FileUploader fileUploader;

    @Override
    @Transactional
    public String uploadAndSave(Long userId, MultipartFile file) throws IOException {
        String uploadedUrl = fileUploader.upload(file);

        var user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setProfileImage(uploadedUrl);
        return uploadedUrl;
    }

    @Override
    @Transactional
    public String resetToDefaultImage(Long userId) {
        var user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        String currentImage = user.getProfileImage();
        // 기본 이미지가 아닐 경우에만 삭제
        if (currentImage != null && !currentImage.equals("/images/default-profile.png")) {
            fileUploader.delete(currentImage); // 실제 파일 삭제
        }

        String defaultUrl = "/images/default-profile.png";
        user.setProfileImage(defaultUrl);
        return defaultUrl;
    }

}
