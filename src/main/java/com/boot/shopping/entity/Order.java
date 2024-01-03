package com.boot.shopping.entity;
//양방향
//on delete cascade - 부모 삭제 시 자식 자동 삭제
//영속성 전이 - 연쇄적으로 어떤 일이 일어나는 것(cascade), Entity상태를 변경할 때 해당 Entity와 연관된 Entity 상태변화를 전파시키는 옵션
import com.boot.shopping.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@ToString
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime orderDate;   //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL
            ,orphanRemoval = true
            ,fetch = FetchType.LAZY)  //orders테이블에서 관리(원래는 OrderItem이 관리)
    private List<OrderItem> orderItems = new ArrayList<>();



    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this); //양방향 참조 관계
    }

    //상품주문 회원정보
    public static Order createOrder(Member member,List<OrderItem> orderItemList){
        Order order=new Order();
        order.setMember(member);
        for(OrderItem orderItem:orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    public int getTotalPrice(){
        int totalPrice=0;
        for(OrderItem orderItem:orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    //주문취소
    public void cancelOrder(){
        this.orderStatus=OrderStatus.CANCEL;
        for(OrderItem orderItem:orderItems){
            orderItem.cancel();
        }
    }
}



























