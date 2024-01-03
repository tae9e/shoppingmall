package com.boot.shopping.entity;

import com.boot.shopping.constant.Role;
import com.boot.shopping.dto.MemberFormDto;
import com.boot.shopping.repository.CartRepository;
import com.boot.shopping.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext //영속성(전체)
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto,passwordEncoder);

    }

    @Test
    @DisplayName("장바구니 회원 Entity Mapping 조회 테스트")
    public void findCartAndMember(){
        Member member = createMember();
        memberRepository.save(member);
        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); //transaction이 끝날 때 flush() 호출해서 DB에 반영
        em.clear();

        //cart id를 찾고 값이 없으면 예외 발생
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        //Cart에 저장된 member id와 member id가 같은 지 비교
        assertEquals(savedCart.getMember().getId(),member.getId());
    }

}