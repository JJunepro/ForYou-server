package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.CodeMstRequestDto;
import com.foryou.foryouserver.dto.CodeMstResponseDto;
import com.foryou.foryouserver.entity.CodeMst;
import com.foryou.foryouserver.repository.CodeMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CodeMstService {
    
    private final CodeMstRepository codeMstRepository;
    
    /**
     * 코드 그룹 생성
     */
    @Transactional
    public CodeMstResponseDto createCodeMst(CodeMstRequestDto requestDto) {
        log.info("코드 그룹 생성 요청: {}", requestDto.getGrpCd());
        
        CodeMst codeMst = CodeMst.builder()
                .grpCd(requestDto.getGrpCd())
                .grpNm(requestDto.getGrpNm())
                .grpCate(requestDto.getGrpCate())
                .grpParentCd(requestDto.getGrpParentCd())
                .grpDesc(requestDto.getGrpDesc())
                .dspSeq(requestDto.getDspSeq() != null ? requestDto.getDspSeq() : 0)
                .useYn(requestDto.getUseYn() != null ? requestDto.getUseYn() : "Y")
                .attr1(requestDto.getAttr1())
                .attr2(requestDto.getAttr2())
                .attr3(requestDto.getAttr3())
                .firId(requestDto.getFirId() != null ? requestDto.getFirId() : 0)
                .lstId(requestDto.getLstId() != null ? requestDto.getLstId() : 0)
                .build();
        
        CodeMst savedCodeMst = codeMstRepository.save(codeMst);
        log.info("코드 그룹 생성 완료: {}", savedCodeMst.getGrpCd());
        
        return CodeMstResponseDto.from(savedCodeMst);
    }
    
    /**
     * 코드 그룹 수정
     */
    @Transactional
    public CodeMstResponseDto updateCodeMst(String grpCd, CodeMstRequestDto requestDto) {
        log.info("코드 그룹 수정 요청: {}", grpCd);
        
        CodeMst codeMst = codeMstRepository.findById(grpCd)
                .orElseThrow(() -> new RuntimeException("코드 그룹을 찾을 수 없습니다: " + grpCd));
        
        codeMst.setGrpNm(requestDto.getGrpNm());
        codeMst.setGrpCate(requestDto.getGrpCate());
        codeMst.setGrpParentCd(requestDto.getGrpParentCd());
        codeMst.setGrpDesc(requestDto.getGrpDesc());
        codeMst.setDspSeq(requestDto.getDspSeq());
        codeMst.setUseYn(requestDto.getUseYn());
        codeMst.setAttr1(requestDto.getAttr1());
        codeMst.setAttr2(requestDto.getAttr2());
        codeMst.setAttr3(requestDto.getAttr3());
        codeMst.setLstId(requestDto.getLstId());
        
        CodeMst updatedCodeMst = codeMstRepository.save(codeMst);
        log.info("코드 그룹 수정 완료: {}", updatedCodeMst.getGrpCd());
        
        return CodeMstResponseDto.from(updatedCodeMst);
    }
    
    /**
     * 코드 그룹 삭제 (사용 여부를 N으로 변경)
     */
    @Transactional
    public void deleteCodeMst(String grpCd) {
        log.info("코드 그룹 삭제 요청: {}", grpCd);
        
        CodeMst codeMst = codeMstRepository.findById(grpCd)
                .orElseThrow(() -> new RuntimeException("코드 그룹을 찾을 수 없습니다: " + grpCd));
        
        codeMst.setUseYn("N");
        codeMstRepository.save(codeMst);
        
        log.info("코드 그룹 삭제 완료: {}", grpCd);
    }
    
    /**
     * 코드 그룹 조회
     */
    public CodeMstResponseDto getCodeMst(String grpCd) {
        log.info("코드 그룹 조회 요청: {}", grpCd);
        
        CodeMst codeMst = codeMstRepository.findByGrpCdAndUseYn(grpCd, "Y")
                .orElseThrow(() -> new RuntimeException("코드 그룹을 찾을 수 없습니다: " + grpCd));
        
        return CodeMstResponseDto.from(codeMst);
    }
    
    /**
     * 모든 코드 그룹 목록 조회
     */
    public List<CodeMstResponseDto> getAllCodeMst() {
        log.info("모든 코드 그룹 목록 조회 요청");
        
        List<CodeMst> codeMstList = codeMstRepository.findByUseYnOrderByDspSeqAsc("Y");
        
        return codeMstList.stream()
                .map(CodeMstResponseDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 카테고리별 코드 그룹 목록 조회
     */
    public List<CodeMstResponseDto> getCodeMstByCategory(String grpCate) {
        log.info("카테고리별 코드 그룹 목록 조회 요청: {}", grpCate);
        
        List<CodeMst> codeMstList = codeMstRepository.findByGrpCateAndUseYnOrderByDspSeqAsc(grpCate, "Y");
        
        return codeMstList.stream()
                .map(CodeMstResponseDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 코드 그룹명으로 검색
     */
    public List<CodeMstResponseDto> searchCodeMstByName(String grpNm) {
        log.info("코드 그룹명으로 검색 요청: {}", grpNm);
        
        List<CodeMst> codeMstList = codeMstRepository.findByGrpNmContainingAndUseYnOrderByDspSeqAsc(grpNm, "Y");
        
        return codeMstList.stream()
                .map(CodeMstResponseDto::from)
                .collect(Collectors.toList());
    }
}
