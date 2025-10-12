package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.CustomOAuth2User;
import com.foryou.foryouserver.dto.GoogleUserInfo;
import com.foryou.foryouserver.dto.KakaoUserInfo;
import com.foryou.foryouserver.dto.OAuth2UserInfo;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemMstRepository memMstRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // OAuth2 제공자 구분 (google, kakao, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAuth2 로그인 시도: provider={}", registrationId);

        OAuth2UserInfo oAuth2UserInfo = null;

        if ("google".equals(registrationId)) {
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
            log.info("Google OAuth2 사용자 정보 파싱 완료");
        } else if ("kakao".equals(registrationId)) {
            oAuth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
            log.info("Kakao OAuth2 사용자 정보 파싱 완료");
        }
        // 추후 네이버 등 추가 가능
        // else if ("naver".equals(registrationId)) {
        //     oAuth2UserInfo = new NaverUserInfo(oauth2User.getAttributes());
        // }

        if (oAuth2UserInfo == null) {
            log.error("지원하지 않는 OAuth2 제공자: {}", registrationId);
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        log.info("OAuth2 사용자 정보: provider={}, providerId={}, email={}, name={}", 
                provider, providerId, email, name);

        // 기존 회원 조회 또는 신규 회원 생성
        Optional<MemMst> memberOptional = memMstRepository.findByProviderAndProviderId(provider, providerId);

        MemMst member;
        if (memberOptional.isPresent()) {
            // 기존 회원 - 로그인
            member = memberOptional.get();
            member.setUpdDt(LocalDateTime.now());
            log.info("기존 OAuth2 회원 로그인: email={}, memKey={}", member.getMemEmail(), member.getMemKey());
        } else {
            // 신규 회원 - 회원가입
            member = new MemMst();
            member.setMemEmail(email != null ? email : provider + "_" + providerId + "@oauth.local");
            member.setMemNick(name != null ? name : (email != null ? email.split("@")[0] : "사용자" + providerId));
            member.setProvider(provider);
            member.setProviderId(providerId);
            member.setMemStatus("Y");
            member.setRegDt(LocalDateTime.now());
            member.setUpdDt(LocalDateTime.now());
            // OAuth2 로그인은 비밀번호 불필요
            member.setMemPwd(null);
            
            log.info("신규 OAuth2 회원 가입: provider={}, email={}", provider, email);
        }

        member = memMstRepository.save(member);
        log.info("OAuth2 회원 정보 저장 완료: memKey={}", member.getMemKey());

        return new CustomOAuth2User(oauth2User, member.getMemKey(), member.getMemEmail());
    }
}
