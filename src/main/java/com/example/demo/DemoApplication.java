package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoApplication {
  public static void main(String[] args) {

    // JVM property 기준으로 판단하도록 수정
    String profile = System.getProperty("spring.profiles.active");

    if ("local".equals(profile)) {
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