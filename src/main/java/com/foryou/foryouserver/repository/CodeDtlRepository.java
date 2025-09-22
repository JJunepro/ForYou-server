package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.CodeDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeDtlRepository extends JpaRepository<CodeDtl, String> {
    
    /**
     * 그룹 코드로 상세 코드 목록 조회 (사용 중인 것만)
     */
    List<CodeDtl> findByGrpCdAndUseYnOrderByDspSeqAsc(String grpCd, String useYn);
    
    /**
     * 그룹 코드와 상위 코드로 하위 코드 목록 조회
     */
    List<CodeDtl> findByGrpCdAndParentCdAndUseYnOrderByDspSeqAsc(String grpCd, String parentCd, String useYn);
    
    /**
     * 특정 상세 코드 조회
     */
    Optional<CodeDtl> findByGrpCdAndCdAndUseYn(String grpCd, String cd, String useYn);
    
    /**
     * 코드명으로 검색
     */
    @Query("SELECT c FROM CodeDtl c WHERE c.cdNm LIKE %:cdNm% AND c.useYn = :useYn ORDER BY c.dspSeq ASC")
    List<CodeDtl> findByCdNmContainingAndUseYnOrderByDspSeqAsc(@Param("cdNm") String cdNm, @Param("useYn") String useYn);
    
    /**
     * 그룹 코드와 코드명으로 검색
     */
    @Query("SELECT c FROM CodeDtl c WHERE c.grpCd = :grpCd AND c.cdNm LIKE %:cdNm% AND c.useYn = :useYn ORDER BY c.dspSeq ASC")
    List<CodeDtl> findByGrpCdAndCdNmContainingAndUseYnOrderByDspSeqAsc(@Param("grpCd") String grpCd, @Param("cdNm") String cdNm, @Param("useYn") String useYn);
}
