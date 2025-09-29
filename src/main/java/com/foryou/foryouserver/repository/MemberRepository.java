package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.MemMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemMst, Long>{
    boolean existsByMemEmail(String memEmail);

    Optional<MemMst> findByMemEmail(String memEmail);
    List<MemMst> findByMemStatus(String memStatus);

}
