package com.foryou.foryouserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 카카오 로그인 요청 DTO (프론트엔드에서 REST API로 호출)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginRequestDto {

    private String providerId;      // 카카오 ID
    private String email;           // 이메일 (선택)
    private String nickname;        // 닉네임
    private String profileImage;    // 프로필 이미지
}

