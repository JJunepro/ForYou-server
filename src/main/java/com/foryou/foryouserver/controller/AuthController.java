package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.SignupRequest;
import com.foryou.foryouserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> singup(@RequestBody SignupRequest dto) {
        memberService.signup(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }
}
