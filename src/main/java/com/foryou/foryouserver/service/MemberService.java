package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.MemberResponse;
import com.foryou.foryouserver.dto.SignupRequest;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest dto) {
        if(memberRepository.existsByMemEmail(dto.getMemEmail())) {
            throw new RuntimeException("이메일이 이미 존재합니다.");
        }
        MemMst memMst = new MemMst();
        memMst.setMemEmail(dto.getMemEmail());
        memMst.setMemNick(dto.getMemNick());
        memMst.setMemPwd(passwordEncoder.encode(dto.getMemPwd()));
        memberRepository.save(memMst);

    }

    /*public MemberResponse getMember(String memEmail) {
        MemMst memMst = memberRepository.findByMemEmail(memEmail)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return MemberResponse.from(memMst);
    }*/

    public List<MemberResponse> getMemberList() {
            return memberRepository.findByMemStatus("Y")
                .stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
}
