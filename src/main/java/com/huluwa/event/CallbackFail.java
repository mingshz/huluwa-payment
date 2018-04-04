package com.huluwa.event;

import lombok.Data;

/**
 * 回调下单成功事件
 */
@Data
public class CallbackFail {

    public CallbackFail(String message){
        this.message = message;
    }
    private String message;
}
