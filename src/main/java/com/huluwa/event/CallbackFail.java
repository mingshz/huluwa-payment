package com.huluwa.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 回调下单成功事件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackFail extends PaymentOrderEvent {

    public CallbackFail(String orderId){
        super(orderId);
    }
    private String message;
}
