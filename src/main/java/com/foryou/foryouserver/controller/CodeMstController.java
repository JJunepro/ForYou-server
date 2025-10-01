package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.ApiResponseDto;
import com.foryou.foryouserver.dto.CodeMstRequestDto;
import com.foryou.foryouserver.dto.CodeMstResponseDto;
import com.foryou.foryouserver.service.CodeMstService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code-mst")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CodeMstController {
    
    private final CodeMstService codeMstService;
    
    /**
     * 코드 그룹 생성
     */
    @PostMapping
    public ResponseEntity<CodeMstResponseDto> createCodeMst(@Valid @RequestBody CodeMstRequestDto requestDto) {
        log.info("코드 그룹 생성 요청: {}", requestDto.getGrpCd());
        try {
            CodeMstResponseDto response = codeMstService.createCodeMst(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("코드 그룹 생성 실패: {}", requestDto.getGrpCd(), e);
            throw e;
        }
    }
    
    /**
     * 코드 그룹 수정
     */
    @PutMapping("/{grpCd}")
    public ResponseEntity<CodeMstResponseDto> updateCodeMst(
            @PathVariable String grpCd, 
            @Valid @RequestBody CodeMstRequestDto requestDto) {
        log.info("코드 그룹 수정 요청: {}", grpCd);
        CodeMstResponseDto response = codeMstService.updateCodeMst(grpCd, requestDto);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 코드 그룹 삭제
     */
    @DeleteMapping("/{grpCd}")
    public ResponseEntity<Void> deleteCodeMst(@PathVariable String grpCd) {
        log.info("코드 그룹 삭제 요청: {}", grpCd);
        codeMstService.deleteCodeMst(grpCd);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 코드 그룹 조회
     */
    @GetMapping("/{grpCd}")
    public ResponseEntity<CodeMstResponseDto> getCodeMst(@PathVariable String grpCd) {
        log.info("코드 그룹 조회 요청: {}", grpCd);
        CodeMstResponseDto response = codeMstService.getCodeMst(grpCd);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 모든 코드 그룹 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<CodeMstResponseDto>> getAllCodeMst() {
        log.info("모든 코드 그룹 목록 조회 요청");
        List<CodeMstResponseDto> response = codeMstService.getAllCodeMst();
        return ResponseEntity.ok(response);
    }
    
    /**
     * 카테고리별 코드 그룹 목록 조회
     */
    @GetMapping("/category/{grpCate}")
    public ResponseEntity<List<CodeMstResponseDto>> getCodeMstByCategory(@PathVariable String grpCate) {
        log.info("카테고리별 코드 그룹 목록 조회 요청: {}", grpCate);
        List<CodeMstResponseDto> response = codeMstService.getCodeMstByCategory(grpCate);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 코드 그룹명으로 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<CodeMstResponseDto>> searchCodeMstByName(@RequestParam String grpNm) {
        log.info("코드 그룹명으로 검색 요청: {}", grpNm);
        List<CodeMstResponseDto> response = codeMstService.searchCodeMstByName(grpNm);
        return ResponseEntity.ok(response);
    }
}
