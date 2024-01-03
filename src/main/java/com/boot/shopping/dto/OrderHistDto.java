package com.boot.shopping.dto;

import com.boot.shopping.constant.OrderStatus;
import com.boot.shopping.entity.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    private Long orderId;
    private String OrderDate;
    private OrderStatus orderStatus;

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.OrderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    //주문 상품 리스트 <=orderItemDto
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
