package com.huluwa.event;


import lombok.Data;

/**
 * 回调,下单失败事件
 */
@Data
public class CallbackSuccess {

    public CallbackSuccess(String message){
        this.message = message;
    }

    private String message;
}
