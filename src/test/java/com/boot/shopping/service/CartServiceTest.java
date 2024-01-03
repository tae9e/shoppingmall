package com.boot.shopping.service;

import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.dto.CartItemDto;
import com.boot.shopping.entity.CartItem;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.Member;
import com.boot.shopping.repository.CartItemRepository;
import com.boot.shopping.repository.ItemRepository;
import com.boot.shopping.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartService cartService;

    public Item saveItem(){
        Item item=new Item();
        item.setItemNm("test 상품입니다.");
        item.setPrice(10000);
        item.setItemDetail("test 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }


    @Test
    @DisplayName("장바구니 담기 테스트")
    void addCart() {
        //Item
        Item item=saveItem();
        //Member
        Member member=saveMember();

        //CartItem
        CartItemDto cartItemDto=new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());
        Long cartItemId=cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        assertEquals(item.getId(),cartItem.getItem().getId());
        //장바구니에 담긴 수량과 실제 수량 비교
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }
}