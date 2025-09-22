package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.CodeDtlRequestDto;
import com.foryou.foryouserver.dto.CodeDtlResponseDto;
import com.foryou.foryouserver.service.CodeDtlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code-dtl")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CodeDtlController {
    
    private final CodeDtlService codeDtlService;
    
    /**
     * 상세 코드 생성
     */
    @PostMapping
    public ResponseEntity<CodeDtlResponseDto> createCodeDtl(@Valid @RequestBody CodeDtlRequestDto requestDto) {
        log.info("상세 코드 생성 요청: {}", requestDto.getCd());
        CodeDtlResponseDto response = codeDtlService.createCodeDtl(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * 상세 코드 수정
     */
    @PutMapping("/{cd}")
    public ResponseEntity<CodeDtlResponseDto> updateCodeDtl(
            @PathVariable String cd, 
            @Valid @RequestBody CodeDtlRequestDto requestDto) {
        log.info("상세 코드 수정 요청: {}", cd);
        CodeDtlResponseDto response = codeDtlService.updateCodeDtl(cd, requestDto);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상세 코드 삭제
     */
    @DeleteMapping("/{cd}")
    public ResponseEntity<Void> deleteCodeDtl(@PathVariable String cd) {
        log.info("상세 코드 삭제 요청: {}", cd);
        codeDtlService.deleteCodeDtl(cd);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 상세 코드 조회
     */
    @GetMapping("/{cd}")
    public ResponseEntity<CodeDtlResponseDto> getCodeDtl(@PathVariable String cd) {
        log.info("상세 코드 조회 요청: {}", cd);
        CodeDtlResponseDto response = codeDtlService.getCodeDtl(cd);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 그룹 코드로 상세 코드 목록 조회
     */
    @GetMapping("/group/{grpCd}")
    public ResponseEntity<List<CodeDtlResponseDto>> getCodeDtlByGrpCd(@PathVariable String grpCd) {
        log.info("그룹 코드로 상세 코드 목록 조회 요청: {}", grpCd);
        List<CodeDtlResponseDto> response = codeDtlService.getCodeDtlByGrpCd(grpCd);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상위 코드로 하위 코드 목록 조회
     */
    @GetMapping("/group/{grpCd}/parent/{parentCd}")
    public ResponseEntity<List<CodeDtlResponseDto>> getCodeDtlByParentCd(
            @PathVariable String grpCd, 
            @PathVariable String parentCd) {
        log.info("상위 코드로 하위 코드 목록 조회 요청: grpCd={}, parentCd={}", grpCd, parentCd);
        List<CodeDtlResponseDto> response = codeDtlService.getCodeDtlByParentCd(grpCd, parentCd);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 코드명으로 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<CodeDtlResponseDto>> searchCodeDtlByName(@RequestParam String cdNm) {
        log.info("코드명으로 검색 요청: {}", cdNm);
        List<CodeDtlResponseDto> response = codeDtlService.searchCodeDtlByName(cdNm);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 그룹 코드와 코드명으로 검색
     */
    @GetMapping("/search/group/{grpCd}")
    public ResponseEntity<List<CodeDtlResponseDto>> searchCodeDtlByGrpCdAndName(
            @PathVariable String grpCd, 
            @RequestParam String cdNm) {
        log.info("그룹 코드와 코드명으로 검색 요청: grpCd={}, cdNm={}", grpCd, cdNm);
        List<CodeDtlResponseDto> response = codeDtlService.searchCodeDtlByGrpCdAndName(grpCd, cdNm);
        return ResponseEntity.ok(response);
    }
}
