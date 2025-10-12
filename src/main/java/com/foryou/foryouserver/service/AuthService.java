package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.AuthResponseDto;
import com.foryou.foryouserver.dto.LoginRequestDto;
import com.foryou.foryouserver.dto.SignupRequestDto;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 인증 서비스 (회원가입, 로그인)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final MemMstRepository memMstRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 (BCrypt 암호화)
     */
    @Transactional
    public AuthResponseDto signup(SignupRequestDto requestDto) {
        log.info("회원가입 요청: email={}", requestDto.getMemEmail());

        // 이메일 중복 체크
        if (memMstRepository.existsByMemEmail(requestDto.getMemEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다: " + requestDto.getMemEmail());
        }

        // 닉네임 중복 체크
        if (memMstRepository.existsByMemNick(requestDto.getMemNick())) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다: " + requestDto.getMemNick());
        }

        // 비밀번호 BCrypt 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getMemPwd());
        log.info("비밀번호 암호화 완료: email={}", requestDto.getMemEmail());

        // 회원 정보 생성
        MemMst memMst = new MemMst();
        memMst.setMemEmail(requestDto.getMemEmail());
        memMst.setMemPwd(encodedPassword);  // 암호화된 비밀번호 저장
        memMst.setMemNick(requestDto.getMemNick());
        memMst.setMemStatus("Y");
        memMst.setProvider(null);  // 일반 회원가입
        memMst.setProviderId(null);
        memMst.setRegDt(LocalDateTime.now());
        memMst.setUpdDt(LocalDateTime.now());

        MemMst savedMem = memMstRepository.save(memMst);
        log.info("회원가입 완료: email={}, memKey={}", savedMem.getMemEmail(), savedMem.getMemKey());

        return AuthResponseDto.fromEntity(savedMem);
    }

    /**
     * 로그인 (BCrypt 검증)
     */
    public AuthResponseDto login(LoginRequestDto requestDto) {
        log.info("로그인 요청: email={}", requestDto.getMemEmail());

        // 이메일로 회원 조회
        MemMst memMst = memMstRepository.findByMemEmail(requestDto.getMemEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 계정 상태 확인
        if (!"Y".equals(memMst.getMemStatus())) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        // OAuth2 회원 체크
        if (memMst.getProvider() != null) {
            throw new RuntimeException("소셜 로그인 계정입니다. " + memMst.getProvider() + " 로그인을 이용해주세요.");
        }

        // 비밀번호 검증 (BCrypt)
        if (!passwordEncoder.matches(requestDto.getMemPwd(), memMst.getMemPwd())) {
            log.warn("로그인 실패 - 비밀번호 불일치: email={}", requestDto.getMemEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        log.info("로그인 성공: email={}, memKey={}", memMst.getMemEmail(), memMst.getMemKey());

        // 추후 JWT 토큰 생성 로직 추가 가능
        return AuthResponseDto.fromEntity(memMst);
    }

    /**
     * 이메일 중복 체크
     */
    public boolean checkEmailDuplicate(String memEmail) {
        return memMstRepository.existsByMemEmail(memEmail);
    }

    /**
     * 닉네임 중복 체크
     */
    public boolean checkNicknameDuplicate(String memNick) {
        return memMstRepository.existsByMemNick(memNick);
    }

    /**
     * 회원 정보 조회 (memKey로)
     */
    public AuthResponseDto getMemberInfo(Long memKey) {
        log.info("회원 정보 조회: memKey={}", memKey);
        
        MemMst memMst = memMstRepository.findById(memKey)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memKey));

        return AuthResponseDto.fromEntity(memMst);
    }

    /**
     * 회원 정보 조회 (이메일로)
     */
    public AuthResponseDto getMemberInfoByEmail(String memEmail) {
        log.info("회원 정보 조회: email={}", memEmail);
        
        MemMst memMst = memMstRepository.findByMemEmail(memEmail)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memEmail));

        return AuthResponseDto.fromEntity(memMst);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long memKey, String oldPassword, String newPassword) {
        log.info("비밀번호 변경 요청: memKey={}", memKey);

        MemMst memMst = memMstRepository.findById(memKey)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(oldPassword, memMst.getMemPwd())) {
            throw new RuntimeException("기존 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);
        memMst.setMemPwd(encodedPassword);
        memMst.setUpdDt(LocalDateTime.now());

        memMstRepository.save(memMst);
        log.info("비밀번호 변경 완료: memKey={}", memKey);
    }
}

