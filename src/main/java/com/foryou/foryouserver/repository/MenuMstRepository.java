package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.MenuMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 메뉴 마스터 Repository
 */
@Repository
public interface MenuMstRepository extends JpaRepository<MenuMst, String> {

    /**
     * 사용 중인 메뉴만 조회 (순서대로)
     */
    List<MenuMst> findByUseYnOrderByMenuSeqAsc(String useYn);

    /**
     * 상위 메뉴 ID로 조회 (하위 메뉴 조회)
     */
    List<MenuMst> findByParentMenuIdAndUseYnOrderByMenuSeqAsc(String parentMenuId, String useYn);

    /**
     * 최상위 메뉴만 조회 (parent_menu_id가 null인 메뉴)
     */
    @Query("SELECT m FROM MenuMst m WHERE m.parentMenuId IS NULL AND m.useYn = :useYn ORDER BY m.menuSeq ASC")
    List<MenuMst> findTopLevelMenus(@Param("useYn") String useYn);

    /**
     * 메뉴 순서로 조회
     */
    List<MenuMst> findAllByOrderByMenuSeqAsc();

    /**
     * 특정 상위 메뉴의 하위 메뉴 개수 조회
     */
    long countByParentMenuIdAndUseYn(String parentMenuId, String useYn);
}

