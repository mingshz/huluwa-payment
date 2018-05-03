package com.huluwa.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单支付失败。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPayFailedEvent extends PaymentOrderEvent {

    public OrderPayFailedEvent(String orderId){
        super(orderId);
    }
    private String message;
}
