package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.CustomOAuth2User;
import com.foryou.foryouserver.dto.GoogleUserInfo;
import com.foryou.foryouserver.dto.OAuth2UserInfo;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // OAuth2 제공자 구분 (google, kakao, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if ("google".equals(registrationId)) {
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }
        // 추후 카카오, 네이버 등 추가 가능
        // else if ("kakao".equals(registrationId)) {
        //     oAuth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
        // }

        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        // 기존 회원 조회 또는 신규 회원 생성
        Optional<MemMst> memberOptional = memberRepository.findByProviderAndProviderId(provider, providerId);

        MemMst member;
        if (memberOptional.isPresent()) {
            // 기존 회원 - 로그인
            member = memberOptional.get();
            member.setUpdDt(LocalDateTime.now());
        } else {
            // 신규 회원 - 회원가입
            member = new MemMst();
            member.setMemEmail(email);
            member.setMemNick(name != null ? name : email.split("@")[0]);
            member.setProvider(provider);
            member.setProviderId(providerId);
            member.setMemStatus("Y");
            member.setRegDt(LocalDateTime.now());
            member.setUpdDt(LocalDateTime.now());
            // OAuth2 로그인은 비밀번호 불필요
            member.setMemPwd(null);
        }

        memberRepository.save(member);

        return new CustomOAuth2User(oauth2User, member.getMemKey(), member.getMemEmail());
    }
}
