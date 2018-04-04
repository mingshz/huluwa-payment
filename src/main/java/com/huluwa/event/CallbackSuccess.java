package com.huluwa.event;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 回调,下单失败事件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackSuccess extends PaymentOrderEvent{

    public CallbackSuccess(String orderId){
        super(orderId);
    }
    private String message;
}
