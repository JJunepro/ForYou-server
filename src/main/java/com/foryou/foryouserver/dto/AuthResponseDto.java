package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.MemMst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 인증 응답 DTO (회원가입/로그인 성공 시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private Long memKey;
    private String memEmail;
    private String memNick;
    private String memStatus;
    private String provider;
    private LocalDateTime regDt;
    
    // 토큰 필드 (추후 JWT 구현 시 사용)
    private String accessToken;
    private String refreshToken;

    /**
     * Entity → DTO 변환
     */
    public static AuthResponseDto fromEntity(MemMst memMst) {
        return AuthResponseDto.builder()
                .memKey(memMst.getMemKey())
                .memEmail(memMst.getMemEmail())
                .memNick(memMst.getMemNick())
                .memStatus(memMst.getMemStatus())
                .provider(memMst.getProvider())
                .regDt(memMst.getRegDt())
                .build();
    }

    /**
     * Entity → DTO 변환 (토큰 포함)
     */
    public static AuthResponseDto fromEntityWithToken(MemMst memMst, String accessToken, String refreshToken) {
        return AuthResponseDto.builder()
                .memKey(memMst.getMemKey())
                .memEmail(memMst.getMemEmail())
                .memNick(memMst.getMemNick())
                .memStatus(memMst.getMemStatus())
                .provider(memMst.getProvider())
                .regDt(memMst.getRegDt())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

