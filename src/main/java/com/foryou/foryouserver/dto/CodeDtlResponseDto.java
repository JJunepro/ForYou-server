package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.CodeDtl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDtlResponseDto {
    
    private String cd;
    private String grpCd;
    private String parentCd;
    private String cdNm;
    private String cdDesc;
    private Integer dspSeq;
    private String useYn;
    private String imgPath;
    private String attr1;
    private String attr2;
    private String attr3;
    private LocalDateTime firDt;
    private Integer firId;
    private LocalDateTime lstDt;
    private Integer lstId;
    
    public static CodeDtlResponseDto from(CodeDtl codeDtl) {
        return CodeDtlResponseDto.builder()
                .cd(codeDtl.getCd())
                .grpCd(codeDtl.getGrpCd())
                .parentCd(codeDtl.getParentCd())
                .cdNm(codeDtl.getCdNm())
                .cdDesc(codeDtl.getCdDesc())
                .dspSeq(codeDtl.getDspSeq())
                .useYn(codeDtl.getUseYn())
                .imgPath(codeDtl.getImgPath())
                .attr1(codeDtl.getAttr1())
                .attr2(codeDtl.getAttr2())
                .attr3(codeDtl.getAttr3())
                .firDt(codeDtl.getFirDt())
                .firId(codeDtl.getFirId())
                .lstDt(codeDtl.getLstDt())
                .lstId(codeDtl.getLstId())
                .build();
    }
}
