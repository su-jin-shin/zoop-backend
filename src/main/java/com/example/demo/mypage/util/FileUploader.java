package com.example.demo.mypage.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploader {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String storedFileName = UUID.randomUUID() + "." + extension;

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, storedFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        return "/uploads/" + storedFileName;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }
        return filename.substring(dotIndex + 1);
    }

    public void delete(String filePath) {
        File file = new File(System.getProperty("user.dir") + filePath);
        if (file.exists()) {
            file.delete(); // 실패해도 예외 던지지 않음 (성공 여부 boolean 반환)
        }
    }

}
