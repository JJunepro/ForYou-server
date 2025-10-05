package com.foryou.foryouserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mem_mst")
@Getter
@Setter
@NoArgsConstructor
public class MemMst {
/*
* 컬럼 길이, nullable 조건을 원래 테이블과 맞춤
* */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mem_key 자동 증가
    @Column(name = "mem_key")
    private Long memKey;

    @Column(name = "mem_email", length = 255, nullable = false, unique = true)
    private String memEmail;

    @Column(name = "mem_nick", length = 100, nullable = false)
    private String memNick;

    @Column(name = "mem_pwd", length = 255)
    private String memPwd;

    // OAuth2 provider (google, kakao, naver 등)
    @Column(name = "provider", length = 50)
    private String provider;

    // OAuth2 provider의 고유 ID
    @Column(name = "provider_id", length = 255)
    private String providerId;

    // 필드는 기본값으로 "Y" 로 설정
    @Column(name = "mem_status", length = 1, nullable = false)
    private String memStatus = "Y";

    // updatable 업데이트시에 수정 불가능 입력시에만 최초 저장 가능하도록
    @Column(name = "reg_dt" , columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime regDt = LocalDateTime.now();

    @Column(name = "upd_dt" , columnDefinition = "TIMESTAMP")
    private LocalDateTime updDt = LocalDateTime.now();

}
