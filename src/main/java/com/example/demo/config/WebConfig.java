package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 정적 리소스 매핑 (예: /uploads/** → 실제 경로)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    // CORS 설정 (프론트에서 API 호출 허용)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        // ──────── [로컬 개발] ────────
                        "http://localhost:3000",

                        // ──────── [배포 프론트(Vercel)] ────────
                        "https://zoop-frontend-sable.vercel.app",
                        "https://zoop-frontend-sable-git-*.vercel.app",

                        // ──────── [새 프로덕션 도메인] ────────
                        "https://zoopzoop.shop",
                        "https://www.zoopzoop.shop",

                        // ↓ Swagger용 IP/포트
                         "http://localhost:8000"
                )
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}



