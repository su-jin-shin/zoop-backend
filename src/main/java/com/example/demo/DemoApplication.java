package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
    System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
    System.setProperty("KAKAO_CLIENT_ID", dotenv.get("KAKAO_CLIENT_ID"));
    System.setProperty("KAKAO_REDIRECT_URI", dotenv.get("KAKAO_REDIRECT_URI"));
    System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
    System.setProperty("SPRING_PROFILES_ACTIVE", dotenv.get("SPRING_PROFILES_ACTIVE"));

    SpringApplication.run(DemoApplication.class, args);
  }
}