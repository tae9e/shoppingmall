package com.boot.shopping.entity;

import com.boot.shopping.constant.ItemSellStatus;

import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity{
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,length = 50)
    private String itemNm;

    @Column(name="price", nullable = false)
    private Integer price;

    @Column(nullable = false)
    private int stockNumber; //재고 수량

    //@Lob: 큰 타입
    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태


    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    //ManyToMany의 기본값은 fetch=FetchType.LAZY
    @ManyToMany //(fetch=FetchType.EAGER) //실무에서는 M:N의 관계는 사용하지 않음 => 연결 테이블을 생성해서 1:N 또는 M:1의 관계로 풀어서 사용
    @JoinTable(name="member_item",
    joinColumns = @JoinColumn(name="member_id"),
    inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Member> member;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm=itemFormDto.getItemNm();
        this.price=itemFormDto.getPrice();
        this.stockNumber= itemFormDto.getStockNumber();
        this.itemDetail= itemFormDto.getItemDetail();
        this.itemSellStatus=itemFormDto.getItemSellStatus();
    }
    //재고감소
    public void removeStock(int stockNumber){
        int restStock=this.stockNumber-stockNumber; //재고수량-주문수량
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다. 현재 재고수량:"+this.stockNumber);
        }
        this.stockNumber=restStock;
    }

    //주문취소 => 상품 재고 증가
    public void addStock(int stockNumber){
        this.stockNumber+=stockNumber;
    }

}
