package com.foryou.foryouserver.util;

import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 기존 평문 비밀번호를 BCrypt로 재암호화하는 마이그레이션 서비스
 * ⚠️ 주의: 한 번만 실행해야 함!
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordMigrationService {

    private final MemMstRepository memMstRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 평문 비밀번호를 BCrypt로 재암호화
     * 
     * ⚠️ 주의사항:
     * 1. 이 메서드는 한 번만 실행해야 합니다
     * 2. 기존 평문 비밀번호를 알고 있어야 합니다
     * 3. 실행 전 반드시 데이터베이스 백업하세요
     */
    @Transactional
    public void migrateAllPasswords() {
        log.warn("⚠️ 비밀번호 마이그레이션 시작 - 평문 → BCrypt");
        
        List<MemMst> allMembers = memMstRepository.findAll();
        int migratedCount = 0;
        int skippedCount = 0;

        for (MemMst member : allMembers) {
            // OAuth2 회원은 건너뛰기
            if (member.getProvider() != null) {
                log.info("OAuth2 회원 건너뛰기: email={}, provider={}", 
                        member.getMemEmail(), member.getProvider());
                skippedCount++;
                continue;
            }

            // 이미 BCrypt로 암호화된 비밀번호인지 확인
            // BCrypt 해시는 "$2a$" 또는 "$2b$"로 시작하고 60자 길이
            if (member.getMemPwd() != null && member.getMemPwd().startsWith("$2")) {
                log.info("이미 암호화된 회원 건너뛰기: email={}", member.getMemEmail());
                skippedCount++;
                continue;
            }

            // 평문 비밀번호를 BCrypt로 암호화
            String plainPassword = member.getMemPwd();
            if (plainPassword == null || plainPassword.isEmpty()) {
                log.warn("비밀번호가 없는 회원: email={}", member.getMemEmail());
                skippedCount++;
                continue;
            }

            String encodedPassword = passwordEncoder.encode(plainPassword);
            member.setMemPwd(encodedPassword);
            memMstRepository.save(member);

            log.info("비밀번호 암호화 완료: email={}, 평문 길이={}, 암호화 길이={}", 
                    member.getMemEmail(), plainPassword.length(), encodedPassword.length());
            migratedCount++;
        }

        log.warn("✅ 비밀번호 마이그레이션 완료: 총 {}명, 암호화 {}명, 건너뜀 {}명", 
                allMembers.size(), migratedCount, skippedCount);
    }

    /**
     * 특정 회원의 비밀번호만 재암호화
     */
    @Transactional
    public void migratePasswordByEmail(String memEmail, String plainPassword) {
        log.info("특정 회원 비밀번호 암호화: email={}", memEmail);
        
        MemMst member = memMstRepository.findByMemEmail(memEmail)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memEmail));

        String encodedPassword = passwordEncoder.encode(plainPassword);
        member.setMemPwd(encodedPassword);
        memMstRepository.save(member);

        log.info("비밀번호 암호화 완료: email={}", memEmail);
    }
}

