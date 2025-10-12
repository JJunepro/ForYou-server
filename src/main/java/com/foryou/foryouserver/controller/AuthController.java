package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.*;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemMstRepository;
import com.foryou.foryouserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * 인증 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AuthController {

    private final AuthService authService;
    private final MemMstRepository memMstRepository;

    /**
     * 회원가입
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        log.info("API 호출: 회원가입 - email={}", requestDto.getMemEmail());
        try {
            AuthResponseDto response = authService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success(response, "회원가입이 완료되었습니다."));
        } catch (Exception e) {
            log.error("회원가입 실패: email={}", requestDto.getMemEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("회원가입 실패: " + e.getMessage()));
        }
    }

    /**
     * 로그인
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("API 호출: 로그인 - email={}", requestDto.getMemEmail());
        try {
            AuthResponseDto response = authService.login(requestDto);
            return ResponseEntity.ok(ApiResponseDto.success(response, "로그인 성공"));
        } catch (Exception e) {
            log.error("로그인 실패: email={}", requestDto.getMemEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDto.error("로그인 실패: " + e.getMessage()));
        }
    }

    /**
     * 이메일 중복 체크
     * GET /api/auth/check-email?email=test@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<Boolean>> checkEmailDuplicate(@RequestParam String email) {
        log.info("API 호출: 이메일 중복 체크 - email={}", email);
        try {
            boolean isDuplicate = authService.checkEmailDuplicate(email);
            return ResponseEntity.ok(ApiResponseDto.success(isDuplicate, 
                    isDuplicate ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다."));
        } catch (Exception e) {
            log.error("이메일 중복 체크 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("이메일 중복 체크 실패: " + e.getMessage()));
        }
    }

    /**
     * 닉네임 중복 체크
     * GET /api/auth/check-nickname?nickname=테스터
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponseDto<Boolean>> checkNicknameDuplicate(@RequestParam String nickname) {
        log.info("API 호출: 닉네임 중복 체크 - nickname={}", nickname);
        try {
            boolean isDuplicate = authService.checkNicknameDuplicate(nickname);
            return ResponseEntity.ok(ApiResponseDto.success(isDuplicate,
                    isDuplicate ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다."));
        } catch (Exception e) {
            log.error("닉네임 중복 체크 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("닉네임 중복 체크 실패: " + e.getMessage()));
        }
    }

    /**
     * 회원 정보 조회
     * GET /api/auth/me/{memKey}
     */
    @GetMapping("/me/{memKey}")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> getMemberInfo(@PathVariable Long memKey) {
        log.info("API 호출: 회원 정보 조회 - memKey={}", memKey);
        try {
            AuthResponseDto response = authService.getMemberInfo(memKey);
            return ResponseEntity.ok(ApiResponseDto.success(response));
        } catch (Exception e) {
            log.error("회원 정보 조회 실패: memKey={}", memKey, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("회원 정보 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 비밀번호 변경
     * PUT /api/auth/change-password
     */
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponseDto<Void>> changePassword(@RequestBody Map<String, String> request) {
        log.info("API 호출: 비밀번호 변경");
        try {
            Long memKey = Long.parseLong(request.get("memKey"));
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            authService.changePassword(memKey, oldPassword, newPassword);
            return ResponseEntity.ok(ApiResponseDto.success(null, "비밀번호가 변경되었습니다."));
        } catch (Exception e) {
            log.error("비밀번호 변경 실패", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("비밀번호 변경 실패: " + e.getMessage()));
        }
    }

    /**
     * 카카오 로그인 (REST API 방식)
     * POST /api/auth/kakao-login
     */
    @PostMapping("/kakao-login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> kakaoLogin(@RequestBody KakaoLoginRequestDto requestDto) {
        log.info("API 호출: 카카오 REST API 로그인 - providerId={}", requestDto.getProviderId());
        
        try {
            // 기존 회원 조회
            Optional<MemMst> existingMember = memMstRepository.findByProviderAndProviderId("kakao", requestDto.getProviderId());

            MemMst member;
            
            if (existingMember.isPresent()) {
                // 기존 회원 - 로그인
                member = existingMember.get();
                member.setUpdDt(LocalDateTime.now());
                log.info("기존 카카오 회원 로그인: email={}", member.getMemEmail());
            } else {
                // 신규 회원 - 자동 가입
                member = new MemMst();
                member.setMemEmail(requestDto.getEmail() != null ? requestDto.getEmail() : 
                        "kakao_" + requestDto.getProviderId() + "@oauth.local");
                member.setMemNick(requestDto.getNickname());
                member.setProvider("kakao");
                member.setProviderId(requestDto.getProviderId());
                member.setMemStatus("Y");
                member.setMemPwd(null);  // OAuth2는 비밀번호 불필요
                member.setRegDt(LocalDateTime.now());
                member.setUpdDt(LocalDateTime.now());
                
                log.info("신규 카카오 회원 자동 가입: email={}", member.getMemEmail());
            }

            // 회원 정보 저장
            member = memMstRepository.save(member);

            // 응답 생성 (추후 JWT 토큰 추가 가능)
            AuthResponseDto response = AuthResponseDto.fromEntity(member);
            
            return ResponseEntity.ok(ApiResponseDto.success(response, "카카오 로그인 성공"));
            
        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("카카오 로그인 실패: " + e.getMessage()));
        }
    }

    /**
     * 로그아웃 (클라이언트에서 토큰 삭제)
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout() {
        log.info("API 호출: 로그아웃");
        // 실제로는 클라이언트에서 토큰을 삭제하면 됨
        // JWT 블랙리스트 구현 시 여기서 처리
        return ResponseEntity.ok(ApiResponseDto.success(null, "로그아웃 되었습니다."));
    }
}

