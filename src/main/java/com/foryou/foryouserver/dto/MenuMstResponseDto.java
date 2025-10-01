package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.MenuMst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 메뉴 마스터 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuMstResponseDto {

    private String menuId;
    private String menuNm;
    private String menuIcon;
    private String menuLink;
    private Integer menuSeq;
    private String parentMenuId;
    private String useYn;
    private LocalDateTime firDt;
    private Long firId;
    private LocalDateTime lstDt;
    private Long lstId;
    
    // 하위 메뉴 목록 (트리 구조)
    private List<MenuMstResponseDto> children;

    /**
     * Entity → DTO 변환
     */
    public static MenuMstResponseDto fromEntity(MenuMst menuMst) {
        return MenuMstResponseDto.builder()
                .menuId(menuMst.getMenuId())
                .menuNm(menuMst.getMenuNm())
                .menuIcon(menuMst.getMenuIcon())
                .menuLink(menuMst.getMenuLink())
                .menuSeq(menuMst.getMenuSeq())
                .parentMenuId(menuMst.getParentMenuId())
                .useYn(menuMst.getUseYn())
                .firDt(menuMst.getFirDt())
                .firId(menuMst.getFirId())
                .lstDt(menuMst.getLstDt())
                .lstId(menuMst.getLstId())
                .build();
    }

    /**
     * Entity List → DTO List 변환
     */
    public static List<MenuMstResponseDto> fromEntityList(List<MenuMst> menuMstList) {
        return menuMstList.stream()
                .map(MenuMstResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}

