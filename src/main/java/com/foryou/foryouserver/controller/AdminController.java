package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.ApiResponseDto;
import com.foryou.foryouserver.util.PasswordMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 관리자 전용 API 컨트롤러
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminController {

    private final PasswordMigrationService passwordMigrationService;

    /**
     * 모든 회원 비밀번호 BCrypt 마이그레이션
     * POST /api/admin/migrate-passwords
     * 
     * ⚠️ 주의: 한 번만 실행해야 합니다!
     */
    @PostMapping("/migrate-passwords")
    public ResponseEntity<ApiResponseDto<Void>> migrateAllPasswords() {
        log.warn("⚠️ API 호출: 전체 비밀번호 마이그레이션");
        try {
            passwordMigrationService.migrateAllPasswords();
            return ResponseEntity.ok(ApiResponseDto.success(null, "비밀번호 마이그레이션이 완료되었습니다."));
        } catch (Exception e) {
            log.error("비밀번호 마이그레이션 실패", e);
            return ResponseEntity.status(500)
                    .body(ApiResponseDto.error("마이그레이션 실패: " + e.getMessage()));
        }
    }

    /**
     * 특정 회원 비밀번호 재설정 (관리자용)
     * POST /api/admin/reset-password
     * 
     * Request Body: { "memEmail": "user@example.com", "newPassword": "newpass123" }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<Void>> resetPassword(@RequestBody Map<String, String> request) {
        String memEmail = request.get("memEmail");
        String newPassword = request.get("newPassword");
        
        log.info("API 호출: 비밀번호 재설정 - email={}", memEmail);
        try {
            passwordMigrationService.migratePasswordByEmail(memEmail, newPassword);
            return ResponseEntity.ok(ApiResponseDto.success(null, "비밀번호가 재설정되었습니다."));
        } catch (Exception e) {
            log.error("비밀번호 재설정 실패: email={}", memEmail, e);
            return ResponseEntity.status(400)
                    .body(ApiResponseDto.error("비밀번호 재설정 실패: " + e.getMessage()));
        }
    }
}

