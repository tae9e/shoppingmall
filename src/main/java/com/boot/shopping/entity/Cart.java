package com.boot.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter @Setter @ToString
public class Cart extends BaseEntity{
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    //연관 관계 Mapping
    //@OneToOne - 회원 Entity와 1:1로 매핑
    //@JoinColumn(name="member_id") - 매핑할 외래키(이름)지정
    //즉시(EAGER) 로딩 - entity를 조회할 때 해당 entity와 mapping된 entity를 한번에 조회
    //지연(LAZY) - FetchType.LAZY
    //@OneToOne - FetchType.EAGER가 default
    @OneToOne(fetch = FetchType.EAGER) //default
    @JoinColumn(name="member_id")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}
