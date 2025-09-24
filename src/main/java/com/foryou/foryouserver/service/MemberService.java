package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.SignupRequest;
import com.foryou.foryouserver.entity.MemMst;
import com.foryou.foryouserver.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
