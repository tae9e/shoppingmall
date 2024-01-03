package com.boot.shopping.entity;

import com.boot.shopping.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    //회원 Entity 저장 시 등록자, 수정자, 등록시간, 수정시간 test
    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username="hong",roles = "USER")   //로그인 한 것으로 간주
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);
        em.flush(); //DB반영
        em.clear(); //초기화

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time: " + member.getRegTime());
        System.out.println("update time: " + member.getUpdateTime());
        System.out.println("create time: " + member.getCreatedBy());
        System.out.println("modified time: " + member.getModifiedBy());

    }

}