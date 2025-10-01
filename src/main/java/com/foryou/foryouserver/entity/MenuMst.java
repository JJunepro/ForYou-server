package com.foryou.foryouserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 메뉴 마스터 테이블 엔티티
 * 관리자/사용자 메뉴 관리
 */
@Entity
@Table(name = "tb_menu_mst")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuMst {

    @Id
    @Column(name = "menu_id", length = 10, nullable = false)
    @Comment("메뉴 ID")
    private String menuId;

    @Column(name = "menu_nm", length = 100, nullable = false)
    @Comment("메뉴명")
    private String menuNm;

    @Column(name = "menu_icon", length = 100)
    @Comment("메뉴 아이콘")
    private String menuIcon;

    @Column(name = "menu_link", length = 100)
    @Comment("메뉴 링크")
    private String menuLink;

    @Column(name = "menu_seq", nullable = false)
    @Comment("메뉴 순서")
    private Integer menuSeq;

    @Column(name = "parent_menu_id", length = 10)
    @Comment("상위 메뉴 ID")
    private String parentMenuId;

    @Column(name = "use_yn", length = 1, nullable = false)
    @Comment("사용 여부")
    @Builder.Default
    private String useYn = "Y";

    @Column(name = "fir_dt", nullable = false, updatable = false)
    @Comment("최초 등록 일시")
    @Builder.Default
    private LocalDateTime firDt = LocalDateTime.now();

    @Column(name = "fir_id", nullable = false, updatable = false)
    @Comment("최초 등록자 ID")
    @Builder.Default
    private Long firId = 0L;

    @Column(name = "lst_dt", nullable = false)
    @Comment("최종 수정 일시")
    @Builder.Default
    private LocalDateTime lstDt = LocalDateTime.now();

    @Column(name = "lst_id", nullable = false)
    @Comment("최종 수정자 ID")
    @Builder.Default
    private Long lstId = 0L;

    /**
     * 업데이트 시 자동으로 최종 수정 일시 갱신
     */
    @PreUpdate
    public void preUpdate() {
        this.lstDt = LocalDateTime.now();
    }
}

