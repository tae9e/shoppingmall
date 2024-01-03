package com.boot.shopping.controller;

import com.boot.shopping.dto.MemberFormDto;
import com.boot.shopping.entity.Member;
import com.boot.shopping.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //1 Model에 새롭게 생성된 MemberFormDto전송
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    //2
    //@Valid => FormDto의 message
    @PostMapping(value = "/new")
    public String newForm(@Valid MemberFormDto memberFormDto,
                          BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    //3
    @GetMapping(value="/login")
    public String loginMember(){

        return "member/memberLoginForm";
    }


    //4
    @GetMapping(value="/login/error")
    public String loginError(Model model){
        model.addAttribute("loginError","아이디, 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }


}