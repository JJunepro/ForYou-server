package com.foryou.foryouserver.dto;

import com.foryou.foryouserver.entity.MemMst;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponse {
    private Long memKey;
    private String memEmail;
    private String memNick;
    private String memStatus;
    private LocalDateTime regDt;
    private LocalDateTime updDt;

    public static MemberResponse from(MemMst memMst) {
        MemberResponse response = new MemberResponse();
        response.setMemKey(memMst.getMemKey());
        response.setMemEmail(memMst.getMemEmail());
        response.setMemNick(memMst.getMemNick());
        response.setMemStatus(memMst.getMemStatus());
        response.setRegDt(memMst.getRegDt());
        response.setUpdDt(memMst.getUpdDt());
        return response;
    }
}
