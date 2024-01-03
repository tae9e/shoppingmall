package com.boot.shopping.repository;

import com.boot.shopping.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //중복 회원 검사
    Member findByEmail(String email);
}
