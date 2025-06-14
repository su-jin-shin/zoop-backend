package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {

    // 로컬 프로파일이면 .env.local 주입
    if ("local".equals(System.getenv("PROFILES"))) {
      Dotenv dotenv = Dotenv.configure()
              .filename(".env.local")
              .ignoreIfMissing()
              .load();
      dotenv.entries().forEach(e ->
              System.setProperty(e.getKey(), e.getValue()));
    }

    SpringApplication.run(DemoApplication.class, args);
  }
}
