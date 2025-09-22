package com.foryou.foryouserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_code_dtl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDtl {
    
    @Id
    @Column(name = "cd", length = 50, nullable = false)
    private String cd;
    
    @Column(name = "grp_cd", length = 50, nullable = false)
    private String grpCd;
    
    @Column(name = "parent_cd", length = 50)
    private String parentCd;
    
    @Column(name = "cd_nm", length = 100)
    private String cdNm;
    
    @Column(name = "cd_desc", columnDefinition = "text")
    private String cdDesc;
    
    @Column(name = "dsp_seq")
    @Builder.Default
    private Integer dspSeq = 0;
    
    @Column(name = "use_yn", length = 1)
    @Builder.Default
    private String useYn = "Y";
    
    @Column(name = "img_path", length = 200)
    private String imgPath;
    
    @Column(name = "attr1", length = 200)
    private String attr1;
    
    @Column(name = "attr2", length = 200)
    private String attr2;
    
    @Column(name = "attr3", length = 200)
    private String attr3;
    
    @CreationTimestamp
    @Column(name = "fir_dt")
    private LocalDateTime firDt;
    
    @Column(name = "fir_id")
    @Builder.Default
    private Integer firId = 0;
    
    @UpdateTimestamp
    @Column(name = "lst_dt")
    private LocalDateTime lstDt;
    
    @Column(name = "lst_id")
    @Builder.Default
    private Integer lstId = 0;
}
