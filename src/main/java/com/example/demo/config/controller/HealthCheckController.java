package com.example.demo.config.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    @Value("${server.env}")
    private String env;

    @Value("${SERVER_NAME:unknown}")   // ← 변경 (기본값 unknown)
    private String serverName;

    @GetMapping("/hc")
    public ResponseEntity<Map<String, String>> healthCheck(HttpServletRequest req) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("serverName",   serverName);
        data.put("serverAddress", req.getLocalAddr());
        data.put("port",          String.valueOf(req.getLocalPort()));
        data.put("env",           env);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/env")
    public ResponseEntity<String> getEnv() {
        return ResponseEntity.ok(env);
    }
}