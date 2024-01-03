package com.boot.shopping.entity;
//양방향 매핑은 '연관관계 주인'을 설정해야 함
//Entity를 양방향 연관관계로 설정하면 객체의 참조는 둘인데 외래키는 하나이므로 둘 중 누가 외래키를 관리할지 결정
/*  1. 연관관계의 주인은 외래키가 있는 곳으로 설정
    2. 연관관계의 주인이 외래키 관리(등록, 수정, 삭제)
    3. 주인이 아닌 쪽  → 연관관계 Mapping 시 mappedBy 속성의 값으로 연관관계 주인 설정
                     → 읽기만 가능*/
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice;

    private int count;  //주문 수량

    @CreatedDate
    private LocalDateTime orderDate; //주문일

    //Auditing으로 인해서 삭제
    /*private LocalDateTime regTime;
    private LocalDateTime updateTime;*/

    //주문할 상품과 수량을 통해 주문 상품을 만듦
    public static OrderItem createOrderItem(Item item,int count){
        OrderItem orderItem=new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        //orderItem.setOrderDate(LocalDateTime.now());
        item.removeStock(count);
        return orderItem;
    }
    //주문상품 총가격
    public int getTotalPrice(){

        return orderPrice*count;
    }
    //주문취소 => 주문 수량만큼 상품재고 증가
    public void cancel(){

        this.getItem().addStock(count);
    }
}
