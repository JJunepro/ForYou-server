package com.foryou.foryouserver.dto;

import java.util.Map;

/**
 * 카카오 OAuth2 사용자 정보 DTO
 */
public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        // 카카오 계정 정보
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        if (kakaoAccount == null) {
            return null;
        }

        // 이메일 제공 동의 확인
        Boolean emailNeedsAgreement = (Boolean) kakaoAccount.get("email_needs_agreement");
        if (emailNeedsAgreement != null && emailNeedsAgreement) {
            return null;
        }

        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        // 카카오 계정 정보
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        if (kakaoAccount == null) {
            return null;
        }

        // 프로필 정보
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    /**
     * 프로필 이미지 URL 가져오기
     */
    public String getProfileImageUrl() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        if (kakaoAccount == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        if (profile == null) {
            return null;
        }

        return (String) profile.get("profile_image_url");
    }

    /**
     * 썸네일 이미지 URL 가져오기
     */
    public String getThumbnailImageUrl() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        if (kakaoAccount == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        if (profile == null) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }
}

