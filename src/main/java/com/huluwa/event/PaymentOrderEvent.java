package com.huluwa.event;

import lombok.Data;

/**
 * 支付订单事件
 * @author CJ
 */
@Data
abstract class PaymentOrderEvent {

    public PaymentOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    private String orderId;
}
