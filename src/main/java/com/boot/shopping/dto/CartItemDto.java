package com.boot.shopping.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CartItemDto {
    @NotNull(message = "상품 아이디는 필수 입력입니다.")
    private Long itemId;
    @Min(value = 1,message = "최소 수량은 1개 이상입니다.")
    private int count;


}
