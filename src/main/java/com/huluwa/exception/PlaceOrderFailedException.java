package com.huluwa.exception;

/**
 * 下单失败时的异常
 */
public class PlaceOrderFailedException extends RuntimeException {

    public PlaceOrderFailedException(){

    }
    public PlaceOrderFailedException(String s){
        super(s);
    }
    public PlaceOrderFailedException(Exception e){
        super(e);
    }
}
