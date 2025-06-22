package com.example.demo.mypage.service;

import com.example.demo.mypage.repository.MypageUserInfoRepository;
import com.example.demo.mypage.util.FileUploader;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.demo.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "fileUploader는 내부에서만 사용됨")
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private static final String DEFAULT_PROFILE_IMAGE = "/images/default-profile.png";

    private final MypageUserInfoRepository userInfoRepository;
    private final FileUploader fileUploader;

    @Override
    @Transactional
    public String uploadAndSave(Long userId, MultipartFile file) throws IOException {
        String uploadedUrl = fileUploader.upload(file);

        var user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setProfileImage(uploadedUrl);
        return uploadedUrl;
    }

    @Override
    @Transactional
    public String resetToDefaultImage(Long userId) {
        var user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        String currentImage = user.getProfileImage();

        if (currentImage != null && !currentImage.equals(DEFAULT_PROFILE_IMAGE)) {
            fileUploader.delete(currentImage); // 실제 파일 삭제
        }

        user.setProfileImage(DEFAULT_PROFILE_IMAGE);
        return DEFAULT_PROFILE_IMAGE;
    }

}
