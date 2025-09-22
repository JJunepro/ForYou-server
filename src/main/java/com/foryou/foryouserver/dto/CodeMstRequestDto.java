package com.foryou.foryouserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeMstRequestDto {
    
    @NotBlank(message = "그룹 코드는 필수입니다")
    @Size(max = 50, message = "그룹 코드는 50자를 초과할 수 없습니다")
    private String grpCd;
    
    @Size(max = 100, message = "그룹명은 100자를 초과할 수 없습니다")
    private String grpNm;
    
    @NotBlank(message = "그룹 카테고리는 필수입니다")
    @Size(max = 50, message = "그룹 카테고리는 50자를 초과할 수 없습니다")
    private String grpCate;
    
    @Size(max = 50, message = "상위 그룹 코드는 50자를 초과할 수 없습니다")
    private String grpParentCd;
    
    private String grpDesc;
    
    private Integer dspSeq;
    
    @Size(max = 1, message = "사용 여부는 1자여야 합니다")
    private String useYn;
    
    @Size(max = 200, message = "추가 속성1은 200자를 초과할 수 없습니다")
    private String attr1;
    
    @Size(max = 200, message = "추가 속성2는 200자를 초과할 수 없습니다")
    private String attr2;
    
    @Size(max = 200, message = "추가 속성3은 200자를 초과할 수 없습니다")
    private String attr3;
    
    private Integer firId;
    private Integer lstId;
}
