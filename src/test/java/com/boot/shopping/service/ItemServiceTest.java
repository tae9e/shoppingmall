package com.boot.shopping.service;


import com.boot.shopping.constant.ItemSellStatus;
import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.ItemImg;
import com.boot.shopping.repository.ItemImgRepository;
import com.boot.shopping.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> creatMultipartFiles(){
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for(int i=0;i<5;i++){
            String path="C:/shop/item";
            String imageName="image"+i+".jpg";
            MockMultipartFile multipartFile
                    = new MockMultipartFile(path,imageName,"image/jpg",new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username="admin",roles = "ADMIN")
    void saveItem() throws IOException {
        ItemFormDto itemFormDto=new ItemFormDto();
        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList=creatMultipartFiles();
        Long itemId= itemService.saveItem(itemFormDto,multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(),item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(),item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(),item.getItemDetail());
        assertEquals(itemFormDto.getPrice(),item.getPrice());
        assertEquals(itemFormDto.getStockNumber(),item.getStockNumber());
       // assertEquals(multipartFileList.get(0).getOriginalFilename(),itemImgList.get(0).getOrigImgName());

    }
}
