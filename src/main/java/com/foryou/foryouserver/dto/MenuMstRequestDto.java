package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.MenuMst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 메뉴 마스터 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuMstRequestDto {

    private String menuId;
    private String menuNm;
    private String menuIcon;
    private String menuLink;
    private Integer menuSeq;
    private String parentMenuId;
    private String useYn;
    private Long lstId;  // 수정자 ID

    /**
     * DTO → Entity 변환 (생성)
     */
    public MenuMst toEntity() {
        return MenuMst.builder()
                .menuId(this.menuId)
                .menuNm(this.menuNm)
                .menuIcon(this.menuIcon)
                .menuLink(this.menuLink)
                .menuSeq(this.menuSeq)
                .parentMenuId(this.parentMenuId)
                .useYn(this.useYn != null ? this.useYn : "Y")
                .firDt(LocalDateTime.now())
                .firId(this.lstId != null ? this.lstId : 0L)
                .lstDt(LocalDateTime.now())
                .lstId(this.lstId != null ? this.lstId : 0L)
                .build();
    }

    /**
     * 기존 Entity 업데이트
     */
    public void updateEntity(MenuMst menuMst) {
        if (this.menuNm != null) menuMst.setMenuNm(this.menuNm);
        if (this.menuIcon != null) menuMst.setMenuIcon(this.menuIcon);
        if (this.menuLink != null) menuMst.setMenuLink(this.menuLink);
        if (this.menuSeq != null) menuMst.setMenuSeq(this.menuSeq);
        if (this.parentMenuId != null) menuMst.setParentMenuId(this.parentMenuId);
        if (this.useYn != null) menuMst.setUseYn(this.useYn);
        if (this.lstId != null) menuMst.setLstId(this.lstId);
        menuMst.setLstDt(LocalDateTime.now());
    }
}

