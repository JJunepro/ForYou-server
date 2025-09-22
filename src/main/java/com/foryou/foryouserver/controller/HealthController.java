package com.foryou.foryouserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "ForYou Server");
        response.put("version", "1.0.0");
        response.put("description", "공통코드 관리 시스템 API 서버");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Spring Boot와 Vite 연동 테스트 성공!");
        response.put("backend", "Spring Boot 3.5.5");
        response.put("database", "PostgreSQL (AWS RDS)");
        response.put("features", "공통코드 관리, CORS 지원, REST API");
        return ResponseEntity.ok(response);
    }
}
