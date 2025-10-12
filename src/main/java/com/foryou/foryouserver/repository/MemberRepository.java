package com.foryou.foryouserver.repository;

import com.foryou.foryouserver.entity.MemMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemMst, Long>{
    /*
    * 기능 : 이메일 중복 체크
    * return : boolean
    * 사용 위치 : MemberService
    * */
    boolean existsByMemEmail(String memEmail);

    /*
    * 기능 : 회원 상태로 리스트 조회
    * return : List<MemMst>
    * 사용 위치 : MemberService
    * */
    List<MemMst> findByMemStatus(String memStatus);

    //
    /*
    * 기능 : OAuth2 로그인용 메서드
    * return : Optional<MemMst>
    * 사용 위치 : CustomOAUTH2UserService.java
    *  */
    Optional<MemMst> findByProviderAndProviderId(String provider, String providerId);
}
