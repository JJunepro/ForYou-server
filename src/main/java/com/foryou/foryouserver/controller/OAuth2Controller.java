package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰 검증 엔드포인트
     */
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isValid = jwtTokenProvider.validateToken(token);

            if (isValid) {
                String email = jwtTokenProvider.getEmail(token);
                Long memKey = jwtTokenProvider.getMemKey(token);

                response.put("valid", true);
                response.put("email", email);
                response.put("memKey", memKey);
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Invalid token");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Token validation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
