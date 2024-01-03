package com.boot.shopping.service;

import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.entity.Item;
import com.boot.shopping.entity.ItemImg;
import com.boot.shopping.repository.ItemImgRepository;
import com.boot.shopping.repository.ItemRepository;
import groovy.util.logging.Log;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

//상품 이미지를 업로드하고 이미지 정보 저장하는 클래스
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    private final ItemRepository itemRepository;


    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {
        String oriImgName=itemImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){

                imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            imgUrl = "/images/item/" + imgName;
        }

        //입력받은 상품 이미지 정보 저장
        itemImg.updateItemImg(imgName, oriImgName,imgUrl);
        itemImgRepository.save(itemImg);
    }


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }

    }


