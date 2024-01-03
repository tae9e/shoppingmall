package com.boot.shopping.service;

import com.boot.shopping.dto.CartDetailDto;
import com.boot.shopping.dto.CartItemDto;
import com.boot.shopping.dto.CartOrderDto;
import com.boot.shopping.dto.OrderDto;
import com.boot.shopping.entity.Cart;
import com.boot.shopping.entity.CartItem;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.Member;
import com.boot.shopping.repository.CartItemRepository;
import com.boot.shopping.repository.CartRepository;
import com.boot.shopping.repository.ItemRepository;
import com.boot.shopping.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        log.info("member{}의 값은?"+member);
        //현재 로그인한 회원의 장바구니
        Cart cart = cartRepository.findByMemberId(member.getId());
        log.info("cart{}의 값은?"+cart);
        //해당 회원의 장바구니 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); //CartItem 생성
            cartItemRepository.save(cartItem); //장바구니에 들어갈 상품 저장
            return cartItem.getId();
        }
    }


    //장바구니 상품 조회
    @Transactional(readOnly=true)
    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        log.info("cartDetailDtoList{}는? " + cartDetailDtoList);
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null){
            return cartDetailDtoList; //빈 리스트 반환
        }
        //상품 정보
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    //현재 로그인한 회원과 해당 장바구니 상품을 저장한 회원이 일치하는지 검사
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email); //로그인 회원
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();  //장바구니 저장한 회원
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    //장바구니 상품의 수량을 update하는 메소드
    public void udpateCartItemCount(int count, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);

    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);


    }

    //주문 logic으로 전달한 (1) orderDtoList생성, (2)주문 logic호출, (3)주문 상품 장바구니에서 제거
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList,String email){
        List<OrderDto> orderDtoList=new ArrayList<>();
        for(CartOrderDto cartOrderDto:cartOrderDtoList){
            CartItem cartItem=cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto=new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        Long orderId=orderService.orders(orderDtoList,email);
        for(CartOrderDto cartOrderDto:cartOrderDtoList){
            CartItem cartItem=cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

           cartItemRepository.delete(cartItem);
        }
        return orderId;
    }

}