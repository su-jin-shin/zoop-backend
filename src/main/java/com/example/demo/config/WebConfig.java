package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 정적 파일 매핑 (/uploads/**)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads")
                .toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        // 로컬 개발
                        "http://localhost:3000",
                        // Vercel 정식 & 프리뷰
                        "https://zoop-frontend-sable.vercel.app",
                        "https://*.vercel.app",
                        // 새 백엔드 도메인
                        "https://zoopzoop.shop",
                        "https://www.zoopzoop.shop"
                )
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}


