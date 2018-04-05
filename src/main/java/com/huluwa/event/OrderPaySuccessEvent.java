package com.huluwa.event;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单支付成功
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPaySuccessEvent extends PaymentOrderEvent{

    public OrderPaySuccessEvent(String orderId){
        super(orderId);
    }
    private String message;
}
