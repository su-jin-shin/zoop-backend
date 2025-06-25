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
                        "http://localhost:3000",
                        "https://zoop-frontend-sable.vercel.app",
                        "https://zoop-frontend-sable-git-*.vercel.app",
                        "http://localhost:8000"    // FastAPI Swagger 등 브라우저 접근 시
                )
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}



