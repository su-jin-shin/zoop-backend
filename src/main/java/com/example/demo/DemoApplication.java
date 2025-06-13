package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  private static void setIfPresent(Dotenv dotenv, String key) {
    // ① 우선 시스템 ENV 에 값이 있으면 사용
    String value = System.getenv(key);
    if (value == null) {
      // ② .env 파일에서 시도 (파일이 없으면 null)
      value = dotenv.get(key);
    }
    if (value != null) {                 // 값이 있을 때만 세팅
      System.setProperty(key, value);
    }
  }
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    setIfPresent(dotenv, "POSTGRES_USER");
    setIfPresent(dotenv, "POSTGRES_PASSWORD");
    setIfPresent(dotenv, "KAKAO_CLIENT_ID");
    setIfPresent(dotenv, "KAKAO_REDIRECT_URI");
    setIfPresent(dotenv, "JWT_SECRET_KEY");
    SpringApplication.run(DemoApplication.class, args);
  }
}