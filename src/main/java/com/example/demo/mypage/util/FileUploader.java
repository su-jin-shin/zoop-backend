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
        if (originalFilename == null) {
            throw new IllegalArgumentException("파일 이름이 없습니다.");
        }
        String extension = getExtension(originalFilename);
        String storedFileName = UUID.randomUUID() + "." + extension;

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("업로드 디렉토리 생성 실패: " + UPLOAD_DIR);
            }
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
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("로컬 파일 삭제 실패: " + filePath);
        }
    }

}
