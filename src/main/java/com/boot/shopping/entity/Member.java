package com.boot.shopping.entity;

import com.boot.shopping.constant.Role;
import com.boot.shopping.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
//관리자와 사용자의 계정 관리
public class Member extends BaseEntity{

    @Id //기본키
    @Column(name = "member_id") //컬럼 이름
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) //중복이 되지 않도록 함
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING) //열거형 type은 String(문자형의 열거형)
    private Role role;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        //암호화된 데이터 넣기
        String password = passwordEncoder.encode(memberFormDto.getPassword()); //security관련 내용
        member.setPassword(password); //비밀번호 암호화

        member.setRole(Role.ADMIN); //관리자 계정
        //member.setRole(Role.USER); //사용자 계정

        return member;

    }


}
