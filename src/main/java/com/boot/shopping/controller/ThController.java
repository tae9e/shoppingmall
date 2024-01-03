package com.boot.shopping.controller;

import com.boot.shopping.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.*;


@Controller
@RequestMapping(value = "/thymeleaf")
public class ThController {

    @GetMapping("/ex01")
    public String hello(Model model){
        model.addAttribute("data","타입리프");
        return "th/ex01";
    }

    @GetMapping("/ex02")
    public String itemDto(Model model,@ModelAttribute ItemDto dto) {
       // itemDto dto = new itemDto();
        dto.setItemNm("테스트 상품");
        dto.setItemDetail("상품 상세 설명");
        dto.setPrice(10000);
        dto.setRegTime(now());
        model.addAttribute("item",dto);

        return "th/ex02";

    }
    @GetMapping("/ex03")
    public String itemDto(Model model) {
        List<ItemDto> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto dto = new ItemDto();
            dto.setItemNm("테스트 상품"+i);
            dto.setItemDetail("상품 상세 설명"+i);
            dto.setPrice(10000*i);
            dto.setRegTime(LocalDateTime.now());

            list.add(dto);
        }

        model.addAttribute("itemDtoList", list);
        return "th/ex03";
    }

    @GetMapping("/ex04")
    public String itemDto2(Model model) {
        List<ItemDto> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto dto = new ItemDto();
            dto.setItemNm("테스트 상품"+i);
            dto.setItemDetail("상품 상세 설명"+i);
            dto.setPrice(10000+i);
            dto.setRegTime(LocalDateTime.now());
            list.add(dto);

        }

        model.addAttribute("itemDtoList", list);
        return "th/ex04";
    }

    @GetMapping("/ex05")
    public String ex05(String param1, String param2, Model model){

       return "th/ex05";
    }
    @GetMapping("/ex06")
    public String ex06(String param1, String param2, Model model){
        model.addAttribute("param1",param1);
        model.addAttribute("param2",param2);
       return "th/ex06";
    }

    @GetMapping("/ex07")
    public String ex07(){

       return "th/ex07";
    }




}


