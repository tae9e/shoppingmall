package com.boot.shopping.controller;

import ch.qos.logback.classic.Logger;
import com.boot.shopping.dto.CartDetailDto;
import com.boot.shopping.dto.CartItemDto;
import com.boot.shopping.dto.CartOrderDto;
import com.boot.shopping.entity.CartItem;
import com.boot.shopping.repository.CartItemRepository;
import com.boot.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model){
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        System.out.println("cartDetailList" + cartDetailList);
        model.addAttribute("cartItems",cartDetailList);
        return "cart/cartList";
    }


     @PostMapping("/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            StringBuilder sb=new StringBuilder();
            List<FieldError> fieldErrors=bindingResult.getFieldErrors();
            for(FieldError fieldError:fieldErrors)
                sb.append(fieldError.getDefaultMessage());
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
         System.out.println("test1");
        String email=principal.getName();

        System.out.println("email?? " + email);
        Long cartItemId;
        try{
            cartItemId=cartService.addCart(cartItemDto,email);
            System.out.println("cartItemId?? " + cartItemId);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
       return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    //장바구니 업데이트
    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId")Long cartItemId,int count,Principal principal){
        if(count<=0){
            return new ResponseEntity<String>("최소 한 개 이상 담아주세요",HttpStatus.BAD_REQUEST);
        }else if(!cartService.validateCartItem(cartItemId,principal.getName())){
            return new ResponseEntity<String>("회원 정보가 일치하지 않습니다.",HttpStatus.FORBIDDEN);
        }
        cartService.udpateCartItemCount(count, cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    //장바구니 삭제
    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId,Principal principal){
        if(!cartService.validateCartItem(cartItemId,principal.getName())){
            return new ResponseEntity<String>("수정권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    //장바구니 상품의 수량을 업데이트 요청 처리
    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){
        List<CartOrderDto> cartOrderDtoList=cartOrderDto.getCartOrderDtoList();
        //주문할 상품을 선택하지 않았는지 체크
        if(cartOrderDtoList == null || cartOrderDtoList.size()== 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요",HttpStatus.FORBIDDEN);
        }
        //주문권한 체크
        for(CartOrderDto cartOrder:cartOrderDtoList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);

    }

}
