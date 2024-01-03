package com.boot.shopping.dto;
//DTO: 자바
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ItemDto {
    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;
    //LocalDateTime: 자바 8에서 등장
    private LocalDateTime regTime;
    private LocalDateTime sellStatTime;
}
