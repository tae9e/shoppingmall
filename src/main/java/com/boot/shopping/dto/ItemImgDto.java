package com.boot.shopping.dto;

import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//이미지 전달 dto
@Getter @Setter
public class ItemImgDto {
    private  Long id;

    private  String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn; //대표 이미지 여부

    private static ModelMapper modelMapper = new ModelMapper();
    //ModelMapper- 화면DTO <=> entity
    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);

    }

}
