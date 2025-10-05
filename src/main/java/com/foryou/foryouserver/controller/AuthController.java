package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.MemberResponse;
import com.foryou.foryouserver.dto.SignupRequest;
import com.foryou.foryouserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getMemberList();
        return ResponseEntity.ok(members);
    }

}
