package com.boot.shopping.service;

import com.boot.shopping.dto.OrderDto;
import com.boot.shopping.dto.OrderHistDto;
import com.boot.shopping.dto.OrderItemDto;
import com.boot.shopping.entity.*;
import com.boot.shopping.repository.ItemImgRepository;
import com.boot.shopping.repository.ItemRepository;
import com.boot.shopping.repository.MemberRepository;
import com.boot.shopping.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto,String email){
        Item item=itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member=memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem=OrderItem.createOrderItem(item, orderDto.getCount()); //주문상품 entity
        orderItemList.add(orderItem);

        Order order=Order.createOrder(member,orderItemList);
        orderRepository.save(order);
        return order.getId();

    }

    //주문목록 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount=orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        //주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO 생성
        for(Order order:orders){
            OrderHistDto orderHistDto= new OrderHistDto(order);
            List<OrderItem> orderItems=order.getOrderItems();
            for(OrderItem orderItem:orderItems){
                ItemImg itemImg=itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto=new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
    }

    //주문 취소
    public boolean validateOrder(Long orderId, String email){
        Member curMember=memberRepository.findByEmail(email);
        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember=order.getMember();

        if(!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())){
            return false;
        }
        return true;
    }

    //주문취소 상태로 변경하면 변경감지 기능에 의해서 트랜잭션이 끝날 때 update실행
    public void cancelOrder(Long orderId){
        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
    //장바구니에서 전달받아 주문 생성
    public Long orders(List<OrderDto> orderDtoList,String email){
        Member member=memberRepository.findByEmail(email);
        List<OrderItem> orderItems=new ArrayList<>();
        for(OrderDto orderDto:orderDtoList){
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem=OrderItem.createOrderItem(item, orderDto.getCount());
            orderItems.add(orderItem);
        }
        Order order=Order.createOrder(member,orderItems);
        orderRepository.save(order);
        return order.getId();
    }

}
