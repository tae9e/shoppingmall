package com.boot.shopping.entity;

import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.repository.ItemRepository;
import com.boot.shopping.repository.MemberRepository;
import com.boot.shopping.repository.OrderItemRepository;
import com.boot.shopping.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

     @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    //고객 주문시 주문Entity =>주문 상품 Entity함께 저장하는 경우
    @Test
    @DisplayName("영속성 전이 테스트")
    public  void cascadeTest(){
        Order order = new Order();
         for(int i=0; i<3; i++){
             Item item = this.createItem();
             itemRepository.save(item);
             OrderItem orderItem = new OrderItem();
             orderItem.setItem(item);
             orderItem.setCount(10);
             orderItem.setOrderPrice(1000);
             orderItem.setOrder(order);
             order.getOrderItems().add(orderItem);// orderItem 엔티티를 oder엔티티에 담아두기
         }
         orderRepository.saveAndFlush(order);// 영속성 컨텍스트에 이쓴 객체들을 DB에 반영(commit)
        em.clear();// 영속성 컨텍스트 초기화

        Order savdOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);//주문 entity를 조회
        //itemOrder 엔티티 3개가 실제로 DB 저장 여부 검사
        assertEquals(3, savdOrder.getOrderItems().size());
    }

    public  Order createOrder(){
        Order order = new Order();
        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);// orderItem 엔티티를 oder엔티티에 담아두기
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }
//Order(부모) 삭제시 OrderItem (자식)삭제 되는지 테스트
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();//orderItem을 삭제하는 쿼리문 출력
        //부모 엔티티와 연관관계가 끊어졌기 때문에 고아 객체를 삭제하는 쿼리문 수행
    }

    //주문 데이터를 먼저 DB저장하고, 저장한  주문 상품 데이터를 조회
   //OrderItem에 @ManyToOne의 default이면  \FetchType.EAGER방식이고
   //OrderItem에서  FetchType.LAZy으로 변경하기 =>Lazy 로딩 테스트

    @Test
    @DisplayName("Lazy 로딩 테스트")
    public  void  lazyLoadingTes(){

        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : "+ orderItem.getOrder().getClass());
    }








}