package com.boot.shopping.repository;

import com.boot.shopping.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //상품의 대표 이미지 찾기
    ItemImg findByItemIdAndRepimgYn(Long itemId,String repimgYn);


}
