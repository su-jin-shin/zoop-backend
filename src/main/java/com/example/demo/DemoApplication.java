package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    // 1) prod/prod-like 환경에선 건너뜀
    if (System.getenv("SPRING_PROFILES_ACTIVE") == null
            || "local".equals(System.getenv("SPRING_PROFILES_ACTIVE"))) {

      // 2) .env 읽어 System properties 보강
      Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
      dotenv.entries().forEach(e ->
              System.setProperty(e.getKey(), e.getValue()));
    }

    SpringApplication.run(DemoApplication.class, args);
  }
}