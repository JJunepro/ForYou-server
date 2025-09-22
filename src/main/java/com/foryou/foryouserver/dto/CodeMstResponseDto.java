package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.CodeMst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeMstResponseDto {
    
    private String grpCd;
    private String grpNm;
    private String grpCate;
    private String grpParentCd;
    private String grpDesc;
    private Integer dspSeq;
    private String useYn;
    private String attr1;
    private String attr2;
    private String attr3;
    private LocalDateTime firDt;
    private Integer firId;
    private LocalDateTime lstDt;
    private Integer lstId;
    
    public static CodeMstResponseDto from(CodeMst codeMst) {
        return CodeMstResponseDto.builder()
                .grpCd(codeMst.getGrpCd())
                .grpNm(codeMst.getGrpNm())
                .grpCate(codeMst.getGrpCate())
                .grpParentCd(codeMst.getGrpParentCd())
                .grpDesc(codeMst.getGrpDesc())
                .dspSeq(codeMst.getDspSeq())
                .useYn(codeMst.getUseYn())
                .attr1(codeMst.getAttr1())
                .attr2(codeMst.getAttr2())
                .attr3(codeMst.getAttr3())
                .firDt(codeMst.getFirDt())
                .firId(codeMst.getFirId())
                .lstDt(codeMst.getLstDt())
                .lstId(codeMst.getLstId())
                .build();
    }
}
