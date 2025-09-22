package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.CodeMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeMstRepository extends JpaRepository<CodeMst, String> {
    
    /**
     * 사용 중인 코드 그룹 목록 조회 (사용 여부가 Y인 것만)
     */
    List<CodeMst> findByUseYnOrderByDspSeqAsc(String useYn);
    
    /**
     * 카테고리별 코드 그룹 목록 조회
     */
    List<CodeMst> findByGrpCateAndUseYnOrderByDspSeqAsc(String grpCate, String useYn);
    
    /**
     * 상위 그룹 코드로 하위 그룹 목록 조회
     */
    List<CodeMst> findByGrpParentCdAndUseYnOrderByDspSeqAsc(String grpParentCd, String useYn);
    
    /**
     * 코드 그룹명으로 검색
     */
    @Query("SELECT c FROM CodeMst c WHERE c.grpNm LIKE %:grpNm% AND c.useYn = :useYn ORDER BY c.dspSeq ASC")
    List<CodeMst> findByGrpNmContainingAndUseYnOrderByDspSeqAsc(@Param("grpNm") String grpNm, @Param("useYn") String useYn);
    
    /**
     * 특정 코드 그룹 조회
     */
    Optional<CodeMst> findByGrpCdAndUseYn(String grpCd, String useYn);
}
