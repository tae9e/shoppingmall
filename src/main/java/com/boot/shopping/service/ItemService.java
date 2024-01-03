package com.boot.shopping.service;

import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.dto.ItemImgDto;
import com.boot.shopping.dto.ItemSearchDto;
import com.boot.shopping.dto.MainItemDto;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.ItemImg;
import com.boot.shopping.repository.ItemImgRepository;
import com.boot.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    //1.상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {
        //상품 등록(저장)
        Item item=itemFormDto.createItem();
        itemRepository.save(item);  //상품 데이터 저장

        //이미지 등록(저장)
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            //첫번째 이미지일 경우 대표 상품 이미지 여부 값
            if(i==0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }
        return item.getId();    //상품 이미지 정보 저장
    }

    //2.등록 상품 불러오기
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList= itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg:itemImgList){
            ItemImgDto itemImgDto=ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto=ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    //2.상품 수정
    public Long updateItem(ItemFormDto itemFormDto,List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds=itemFormDto.getItemImgIds();
        //2.이미지 수정(등록)
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }
        return item.getId();
    }

    //상품관리
    //상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품 데이터 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }


    }


