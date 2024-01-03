package com.boot.shopping.controller;

import com.boot.shopping.dto.OrderDto;
import com.boot.shopping.dto.OrderHistDto;
import com.boot.shopping.service.OrderService;
import groovy.util.logging.Log4j2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    //@ResponseBody: 날라오는 데이터를 응답데이터로 대체
    //ResponseEntity: Data + Header(String만)
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb=new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for(FieldError fieldError:fieldErrorList){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email=principal.getName();
        System.out.println("email값: " + email);
        Long orderId;
        try{
            orderId = orderService.order(orderDto,email);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

    //구매이력 조회
    //Principal(Spring Security에 대한 정보)
    @GetMapping(value={"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable= PageRequest.of(page.isPresent()?page.get():0,4); //한 번 가지고 올 주문 갯수
        Page<OrderHistDto> orderHistDtoList=orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders",orderHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderHist";
    }

    //주문취소
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId,Principal principal){
        if(!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

}
