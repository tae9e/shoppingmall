package com.boot.shopping.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Main 페이지에서 데이터를 주고받는 dto
public class MainItemDto {
   private Long id;
   private String itemNm;
   private String itemDetail;
   private String imgUrl;
   private Integer price;

   //Querydsl로 결과 조회 시 MainItemDto객체로 받아오기
   @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
