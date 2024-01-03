package com.boot.shopping.exception;
//주문 시 재고 감소
public class OutOfStockException extends RuntimeException{
    //코드의 재사용성
    public OutOfStockException(String message) {
        super(message);
    }
}
