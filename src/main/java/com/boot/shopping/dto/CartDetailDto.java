package com.boot.shopping.dto;

import lombok.Getter;
import lombok.Setter;
//장바구니 페이지에 전달할 내용
@Getter @Setter
public class CartDetailDto {
    private Long cartItemId; //장바구니 상품 id
    private String itemNm; //상품명
    private int price;
    private int count;
    private String imgUrl;

    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }


}
