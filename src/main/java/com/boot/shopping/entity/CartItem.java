package com.boot.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="cart_item")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    //다대일 관계(해당 클래스가 기준)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch=FetchType.LAZY)  //default - (fetch = FetchType.EAGER)
    @JoinColumn(name="item_id")
    private Item item;

    @Column(name="count")
    private int count;

    public static CartItem createCartItem(Cart cart,Item item,int count){
        CartItem cartItem=new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    //장바구니 상품+추가상품 담을 때 필요한 메소드(기존 수량+담을 수량)
    public void addCount(int count){
        this.count += count;

    }
    //장바구니 수량변경
    public void updateCount(int count){
        this.count = count;
    }


}
