package com.foryou.foryouserver.service;

import com.foryou.foryouserver.dto.MenuMstRequestDto;
import com.foryou.foryouserver.dto.MenuMstResponseDto;
import com.foryou.foryouserver.entity.MenuMst;
import com.foryou.foryouserver.repository.MenuMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 메뉴 마스터 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MenuMstService {

    private final MenuMstRepository menuMstRepository;

    /**
     * 모든 메뉴 조회
     */
    public List<MenuMstResponseDto> getAllMenus() {
        log.info("모든 메뉴 조회");
        List<MenuMst> menuList = menuMstRepository.findAllByOrderByMenuSeqAsc();
        return MenuMstResponseDto.fromEntityList(menuList);
    }

    /**
     * 사용 중인 메뉴만 조회
     */
    public List<MenuMstResponseDto> getActiveMenus() {
        log.info("사용 중인 메뉴 조회");
        List<MenuMst> menuList = menuMstRepository.findByUseYnOrderByMenuSeqAsc("Y");
        return MenuMstResponseDto.fromEntityList(menuList);
    }

    /**
     * 트리 구조로 메뉴 조회 (계층형)
     */
    public List<MenuMstResponseDto> getMenuTree() {
        log.info("메뉴 트리 구조 조회");
        List<MenuMst> allMenus = menuMstRepository.findByUseYnOrderByMenuSeqAsc("Y");
        return buildMenuTree(allMenus, null);
    }

    /**
     * 메뉴 트리 구축 (재귀)
     */
    private List<MenuMstResponseDto> buildMenuTree(List<MenuMst> allMenus, String parentId) {
        return allMenus.stream()
                .filter(menu -> {
                    if (parentId == null) {
                        return menu.getParentMenuId() == null;
                    }
                    return parentId.equals(menu.getParentMenuId());
                })
                .map(menu -> {
                    MenuMstResponseDto dto = MenuMstResponseDto.fromEntity(menu);
                    List<MenuMstResponseDto> children = buildMenuTree(allMenus, menu.getMenuId());
                    dto.setChildren(children.isEmpty() ? null : children);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 ID로 조회
     */
    public MenuMstResponseDto getMenuById(String menuId) {
        log.info("메뉴 조회: menuId={}", menuId);
        MenuMst menuMst = menuMstRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다: " + menuId));
        return MenuMstResponseDto.fromEntity(menuMst);
    }

    /**
     * 메뉴 생성
     */
    @Transactional
    public MenuMstResponseDto createMenu(MenuMstRequestDto requestDto) {
        log.info("메뉴 생성: {}", requestDto);
        
        // 메뉴 ID 중복 체크
        if (menuMstRepository.existsById(requestDto.getMenuId())) {
            throw new RuntimeException("이미 존재하는 메뉴 ID입니다: " + requestDto.getMenuId());
        }

        MenuMst menuMst = requestDto.toEntity();
        MenuMst savedMenu = menuMstRepository.save(menuMst);
        log.info("메뉴 생성 완료: menuId={}", savedMenu.getMenuId());
        
        return MenuMstResponseDto.fromEntity(savedMenu);
    }

    /**
     * 메뉴 수정
     */
    @Transactional
    public MenuMstResponseDto updateMenu(String menuId, MenuMstRequestDto requestDto) {
        log.info("메뉴 수정: menuId={}, data={}", menuId, requestDto);
        
        MenuMst menuMst = menuMstRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다: " + menuId));

        requestDto.updateEntity(menuMst);
        MenuMst updatedMenu = menuMstRepository.save(menuMst);
        log.info("메뉴 수정 완료: menuId={}", updatedMenu.getMenuId());
        
        return MenuMstResponseDto.fromEntity(updatedMenu);
    }

    /**
     * 메뉴 삭제 (논리적 삭제 - use_yn = 'N')
     */
    @Transactional
    public void deleteMenu(String menuId) {
        log.info("메뉴 삭제: menuId={}", menuId);
        
        MenuMst menuMst = menuMstRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다: " + menuId));

        // 하위 메뉴 존재 여부 확인
        long childCount = menuMstRepository.countByParentMenuIdAndUseYn(menuId, "Y");
        if (childCount > 0) {
            throw new RuntimeException("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }

        menuMst.setUseYn("N");
        menuMstRepository.save(menuMst);
        log.info("메뉴 삭제 완료: menuId={}", menuId);
    }

    /**
     * 메뉴 물리적 삭제
     */
    @Transactional
    public void permanentDeleteMenu(String menuId) {
        log.info("메뉴 영구 삭제: menuId={}", menuId);
        
        // 하위 메뉴 존재 여부 확인
        long childCount = menuMstRepository.countByParentMenuIdAndUseYn(menuId, "Y");
        if (childCount > 0) {
            throw new RuntimeException("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }

        menuMstRepository.deleteById(menuId);
        log.info("메뉴 영구 삭제 완료: menuId={}", menuId);
    }
}

