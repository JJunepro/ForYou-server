package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.CodeDtlRequestDto;
import com.foryou.foryouserver.dto.CodeDtlResponseDto;
import com.foryou.foryouserver.entity.CodeDtl;
import com.foryou.foryouserver.repository.CodeDtlRepository;
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
public class CodeDtlService {
    
    private final CodeDtlRepository codeDtlRepository;
    
    /**
     * 상세 코드 생성
     */
    @Transactional
    public CodeDtlResponseDto createCodeDtl(CodeDtlRequestDto requestDto) {
        log.info("상세 코드 생성 요청: {}", requestDto.getCd());
        
        CodeDtl codeDtl = CodeDtl.builder()
                .cd(requestDto.getCd())
                .grpCd(requestDto.getGrpCd())
                .parentCd(requestDto.getParentCd())
                .cdNm(requestDto.getCdNm())
                .cdDesc(requestDto.getCdDesc())
                .dspSeq(requestDto.getDspSeq() != null ? requestDto.getDspSeq() : 0)
                .useYn(requestDto.getUseYn() != null ? requestDto.getUseYn() : "Y")
                .imgPath(requestDto.getImgPath())
                .attr1(requestDto.getAttr1())
                .attr2(requestDto.getAttr2())
                .attr3(requestDto.getAttr3())
                .firId(requestDto.getFirId() != null ? requestDto.getFirId() : 0)
                .lstId(requestDto.getLstId() != null ? requestDto.getLstId() : 0)
                .build();
        
        CodeDtl savedCodeDtl = codeDtlRepository.save(codeDtl);
        log.info("상세 코드 생성 완료: {}", savedCodeDtl.getCd());
        
        return CodeDtlResponseDto.from(savedCodeDtl);
    }
    
    /**
     * 상세 코드 수정
     */
    @Transactional
    public CodeDtlResponseDto updateCodeDtl(String cd, CodeDtlRequestDto requestDto) {
        log.info("상세 코드 수정 요청: {}", cd);
        
        CodeDtl codeDtl = codeDtlRepository.findById(cd)
                .orElseThrow(() -> new RuntimeException("상세 코드를 찾을 수 없습니다: " + cd));
        
        codeDtl.setGrpCd(requestDto.getGrpCd());
        codeDtl.setParentCd(requestDto.getParentCd());
        codeDtl.setCdNm(requestDto.getCdNm());
        codeDtl.setCdDesc(requestDto.getCdDesc());
        codeDtl.setDspSeq(requestDto.getDspSeq());
        codeDtl.setUseYn(requestDto.getUseYn());
        codeDtl.setImgPath(requestDto.getImgPath());
        codeDtl.setAttr1(requestDto.getAttr1());
        codeDtl.setAttr2(requestDto.getAttr2());
        codeDtl.setAttr3(requestDto.getAttr3());
        codeDtl.setLstId(requestDto.getLstId());
        
        CodeDtl updatedCodeDtl = codeDtlRepository.save(codeDtl);
        log.info("상세 코드 수정 완료: {}", updatedCodeDtl.getCd());
        
        return CodeDtlResponseDto.from(updatedCodeDtl);
    }
    
    /**
     * 상세 코드 삭제 (사용 여부를 N으로 변경)
     */
    @Transactional
    public void deleteCodeDtl(String cd) {
        log.info("상세 코드 삭제 요청: {}", cd);
        
        CodeDtl codeDtl = codeDtlRepository.findById(cd)
                .orElseThrow(() -> new RuntimeException("상세 코드를 찾을 수 없습니다: " + cd));
        
        codeDtl.setUseYn("N");
        codeDtlRepository.save(codeDtl);
        
        log.info("상세 코드 삭제 완료: {}", cd);
    }
    
    /**
     * 상세 코드 조회
     */
    public CodeDtlResponseDto getCodeDtl(String cd) {
        log.info("상세 코드 조회 요청: {}", cd);
        
        CodeDtl codeDtl = codeDtlRepository.findById(cd)
                .orElseThrow(() -> new RuntimeException("상세 코드를 찾을 수 없습니다: " + cd));
        
        return CodeDtlResponseDto.from(codeDtl);
    }
    
    /**
     * 그룹 코드로 상세 코드 목록 조회
     */
    public List<CodeDtlResponseDto> getCodeDtlByGrpCd(String grpCd) {
        log.info("그룹 코드로 상세 코드 목록 조회 요청: {}", grpCd);
        
        List<CodeDtl> codeDtlList = codeDtlRepository.findByGrpCdAndUseYnOrderByDspSeqAsc(grpCd, "Y");
        
        return codeDtlList.stream()
                .map(CodeDtlResponseDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 그룹 코드와 상위 코드로 하위 코드 목록 조회
     */
    public List<CodeDtlResponseDto> getCodeDtlByParentCd(String grpCd, String parentCd) {
        log.info("상위 코드로 하위 코드 목록 조회 요청: grpCd={}, parentCd={}", grpCd, parentCd);
        
        List<CodeDtl> codeDtlList = codeDtlRepository.findByGrpCdAndParentCdAndUseYnOrderByDspSeqAsc(grpCd, parentCd, "Y");
        
        return codeDtlList.stream()
                .map(CodeDtlResponseDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 코드명으로 검색
     */
    public List<CodeDtlResponseDto> searchCodeDtlByName(String cdNm) {
        log.info("코드명으로 검색 요청: {}", cdNm);
        
        List<CodeDtl> codeDtlList = codeDtlRepository.findByCdNmContainingAndUseYnOrderByDspSeqAsc(cdNm, "Y");
        
        return codeDtlList.stream()
                .map(CodeDtlResponseDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 그룹 코드와 코드명으로 검색
     */
    public List<CodeDtlResponseDto> searchCodeDtlByGrpCdAndName(String grpCd, String cdNm) {
        log.info("그룹 코드와 코드명으로 검색 요청: grpCd={}, cdNm={}", grpCd, cdNm);
        
        List<CodeDtl> codeDtlList = codeDtlRepository.findByGrpCdAndCdNmContainingAndUseYnOrderByDspSeqAsc(grpCd, cdNm, "Y");
        
        return codeDtlList.stream()
                .map(CodeDtlResponseDto::from)
                .collect(Collectors.toList());
    }
}
