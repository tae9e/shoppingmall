package com.boot.shopping.service;

import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.constant.OrderStatus;
import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.dto.ItemImgDto;
import com.boot.shopping.dto.OrderDto;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.Member;
import com.boot.shopping.entity.Order;
import com.boot.shopping.entity.OrderItem;
import com.boot.shopping.repository.ItemRepository;
import com.boot.shopping.repository.MemberRepository;
import com.boot.shopping.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static com.boot.shopping.entity.Member.createMember;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;

    //주문할 상품, 회원정보를 저장
    public Item saveItem(){
       Item item = new Item();
       item.setItemNm("상품");
        item.setItemDetail("상품 상세 설명");
        item.setPrice(3000);
        item.setStockNumber(15);
        item.setItemSellStatus(ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member=new Member();
        member.setEmail("test@com");
        return memberRepository.save(member);
    }



    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member Member = saveMember();
        OrderDto orderDto=new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId= orderService.order(orderDto, Member.getEmail());
        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDto.getCount()*item.getPrice();

        assertEquals(totalPrice,order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveItem();
        Member Member = saveMember();
        OrderDto orderDto=new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId= orderService.order(orderDto, Member.getEmail());
        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);
        assertEquals(OrderStatus.CANCEL,order.getOrderStatus());
        assertEquals(15,item.getStockNumber());

    }
}