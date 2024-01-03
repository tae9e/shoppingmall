package com.boot.shopping.controller;

import com.boot.shopping.dto.MemberFormDto;
import com.boot.shopping.entity.Member;
import com.boot.shopping.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc //Test용 가짜 MVC
@Transactional //오고 가는 것 일치
//외부 내용 참조
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;


    public Member createMember(String email,String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto,passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void successTest() throws Exception {
        String email="test@com";
        String password="12340000";
        this.createMember(email,password);
        mockMvc.perform(formLogin()
                .userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void successFailTest() throws Exception {
        String email="test@com";
        String password="1234";
        this.createMember(email,password);
        mockMvc.perform(formLogin()
                        .userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password("12345678"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }




}