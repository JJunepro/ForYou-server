package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.MemMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemMst, Long>{
    boolean existsByMemEmail(String memEmail);
}
