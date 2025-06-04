package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class DemoApplication {
//  public static void main(String[] args) {
//    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
//    System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
//    System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
//    SpringApplication.run(DemoApplication.class, args);
//  }
//}


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
