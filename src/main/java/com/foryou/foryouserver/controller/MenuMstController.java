package com.foryou.foryouserver.controller;

import com.foryou.foryouserver.dto.ApiResponseDto;
import com.foryou.foryouserver.dto.MenuMstRequestDto;
import com.foryou.foryouserver.dto.MenuMstResponseDto;
import com.foryou.foryouserver.service.MenuMstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 메뉴 마스터 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class MenuMstController {

    private final MenuMstService menuMstService;

    /**
     * 모든 메뉴 조회
     * GET /api/menus
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<MenuMstResponseDto>>> getAllMenus() {
        log.info("API 호출: 모든 메뉴 조회");
        try {
            List<MenuMstResponseDto> menus = menuMstService.getAllMenus();
            return ResponseEntity.ok(ApiResponseDto.success(menus, menus.size()));
        } catch (Exception e) {
            log.error("메뉴 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("메뉴 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 사용 중인 메뉴만 조회
     * GET /api/menus/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponseDto<List<MenuMstResponseDto>>> getActiveMenus() {
        log.info("API 호출: 사용 중인 메뉴 조회");
        try {
            List<MenuMstResponseDto> menus = menuMstService.getActiveMenus();
            return ResponseEntity.ok(ApiResponseDto.success(menus, menus.size()));
        } catch (Exception e) {
            log.error("메뉴 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("메뉴 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 트리 구조로 메뉴 조회
     * GET /api/menus/tree
     */
    @GetMapping("/tree")
    public ResponseEntity<ApiResponseDto<List<MenuMstResponseDto>>> getMenuTree() {
        log.info("API 호출: 메뉴 트리 구조 조회");
        try {
            List<MenuMstResponseDto> menuTree = menuMstService.getMenuTree();
            return ResponseEntity.ok(ApiResponseDto.success(menuTree));
        } catch (Exception e) {
            log.error("메뉴 트리 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("메뉴 트리 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 메뉴 ID로 조회
     * GET /api/menus/{menuId}
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuMstResponseDto>> getMenuById(@PathVariable String menuId) {
        log.info("API 호출: 메뉴 조회 - menuId={}", menuId);
        try {
            MenuMstResponseDto menu = menuMstService.getMenuById(menuId);
            return ResponseEntity.ok(ApiResponseDto.success(menu));
        } catch (Exception e) {
            log.error("메뉴 조회 실패: menuId={}", menuId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("메뉴 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 메뉴 생성
     * POST /api/menus
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<MenuMstResponseDto>> createMenu(@RequestBody MenuMstRequestDto requestDto) {
        log.info("API 호출: 메뉴 생성 - {}", requestDto);
        try {
            MenuMstResponseDto createdMenu = menuMstService.createMenu(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success(createdMenu, "메뉴가 성공적으로 생성되었습니다."));
        } catch (Exception e) {
            log.error("메뉴 생성 실패", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("메뉴 생성 실패: " + e.getMessage()));
        }
    }

    /**
     * 메뉴 수정
     * PUT /api/menus/{menuId}
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuMstResponseDto>> updateMenu(
            @PathVariable String menuId,
            @RequestBody MenuMstRequestDto requestDto) {
        log.info("API 호출: 메뉴 수정 - menuId={}, data={}", menuId, requestDto);
        try {
            MenuMstResponseDto updatedMenu = menuMstService.updateMenu(menuId, requestDto);
            return ResponseEntity.ok(ApiResponseDto.success(updatedMenu, "메뉴가 성공적으로 수정되었습니다."));
        } catch (Exception e) {
            log.error("메뉴 수정 실패: menuId={}", menuId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("메뉴 수정 실패: " + e.getMessage()));
        }
    }

    /**
     * 메뉴 삭제 (논리적 삭제)
     * DELETE /api/menus/{menuId}
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteMenu(@PathVariable String menuId) {
        log.info("API 호출: 메뉴 삭제 - menuId={}", menuId);
        try {
            menuMstService.deleteMenu(menuId);
            return ResponseEntity.ok(ApiResponseDto.success(null, "메뉴가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("메뉴 삭제 실패: menuId={}", menuId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("메뉴 삭제 실패: " + e.getMessage()));
        }
    }

    /**
     * 메뉴 영구 삭제
     * DELETE /api/menus/{menuId}/permanent
     */
    @DeleteMapping("/{menuId}/permanent")
    public ResponseEntity<ApiResponseDto<Void>> permanentDeleteMenu(@PathVariable String menuId) {
        log.info("API 호출: 메뉴 영구 삭제 - menuId={}", menuId);
        try {
            menuMstService.permanentDeleteMenu(menuId);
            return ResponseEntity.ok(ApiResponseDto.success(null, "메뉴가 영구적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("메뉴 영구 삭제 실패: menuId={}", menuId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("메뉴 영구 삭제 실패: " + e.getMessage()));
        }
    }
}

