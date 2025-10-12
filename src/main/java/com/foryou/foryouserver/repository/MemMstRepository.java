package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.MemMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 마스터 Repository
 */
@Repository
public interface MemMstRepository extends JpaRepository<MemMst, Long> {

    /**
     * 이메일로 회원 조회
     */
    Optional<MemMst> findByMemEmail(String memEmail);

    /**
     * 이메일 중복 체크
     */
    boolean existsByMemEmail(String memEmail);

    /**
     * 닉네임 중복 체크
     */
    boolean existsByMemNick(String memNick);

    /**
     * 이메일과 상태로 회원 조회
     */
    Optional<MemMst> findByMemEmailAndMemStatus(String memEmail, String memStatus);

    /**
     * Provider와 ProviderId로 OAuth2 회원 조회
     */
    Optional<MemMst> findByProviderAndProviderId(String provider, String providerId);
}

