package com.boot.shopping.dto;
//상품 데이터 정보 전달dto

import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {
    private  Long id;
    @NotBlank(message = "상품명은 필수 입력값입니다.")
    private  String itemNm;
    @NotNull(message = "가격은 필수 입력입니다.")
    private Integer price;
    @NotBlank(message = "상품 상세명은 필수 입력값입니다.")
    private String itemDetail;
    @NotNull(message = "재고는 필수 입력값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();


    //entity객체 <==> DTO객체 변환(복사)
    public Item createItem(){

        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){

        return modelMapper.map(item, ItemFormDto.class);
    }

}
