package com.boot.shopping.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//Auditing(감시)-Entity가 저장,수정 시 자동으로 등록일, 수정일, 등록자, 수정자를 기록(입력)
//              "공통 속성 공통화"
public class AuditorAwareImpl implements AuditorAware<String> {
    //현재 로그인한 사용자의 정보를 조회해서 사용자 이름을 등록자와 수정자로 지정
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication!=null){
            userId=authentication.getName();
        }
        return Optional.of(userId);
    }
}
