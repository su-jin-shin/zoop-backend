package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** ───────────────────────────────────────────────
     *  정적 리소스 매핑  (예:  GET /uploads/abc.png → 실제 파일)
     *  프로젝트 루트 하위에 uploads 디렉터리가 있다고 가정
     *  ─────────────────────────────────────────────── */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads")
                .toAbsolutePath()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    /** ───────────────────────────────────────────────
     *  CORS 설정  (프런트엔드 도메인에서 API 호출 허용)
     *  ─────────────────────────────────────────────── */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                /* ‼️  allowedOrigins → allowedOriginPatterns 로 변경
                 *      와일드카드(*)로 Vercel 프리뷰 URL 전부 허용   */
                .allowedOriginPatterns(
                        "https://zoop-frontend-sable.vercel.app",
                        "https://zoop-frontend-sable-git-*.vercel.app",
                        "http://localhost:8000"    // FastAPI Swagger 등 브라우저 접근 시
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")          // * 하나면 Authorization 포함 전부 허용
                .allowCredentials(true);     // 쿠키·세션이 필요하면 true 로
    }
}
