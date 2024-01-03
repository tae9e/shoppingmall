package com.boot.shopping.controller;

import com.boot.shopping.dto.ItemFormDto;
import com.boot.shopping.dto.ItemSearchDto;
import com.boot.shopping.entity.Item;
import com.boot.shopping.service.ItemService;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    //1.상품 등록
    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new") //BindingResult 연결 상태, List<MultipartFile> 이미지+데이터
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if(bindingResult.hasErrors())
            return "item/itemForm";

        if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //2.상품 수정
    //2-1
    @GetMapping("/admin/item/{itemId}")
    public String getItemDtl(@PathVariable("itemId")Long itemId, Model model){
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
        }
            return "item/itemForm";


    }

    //상품 수정(수정하려면 값을 가지고 가야 함)
    //2-2
    @PostMapping ("/admin/item/{itemid}")
        public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
            if(bindingResult.hasErrors())
                return "item/itemForm";

            if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null) {
                model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
                return "item/itemForm";
            }

            try {
                itemService.updateItem(itemFormDto, itemImgFileList);
            }catch(Exception e){
                model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
                return "item/itemForm";
            }
            return "redirect:/";
        }

        //3.상품관리
    @GetMapping(value={"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable= PageRequest.of(page.isPresent()? page.get() : 0,3);
        Page<Item> items=itemService.getAdminItemPage(itemSearchDto,pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);

        return "item/itemMng";
    }

    //5.상품 상세 페이지 <= 메인페이지에서 클릭
    @GetMapping("/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("item",new ItemFormDto());
            return "item/itemDtl";
        }
        return "item/itemDtl";
    }
    }





