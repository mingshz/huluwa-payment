package com.huluwa.service;

import com.huluwa.event.OrderPaySuccessEvent;
import com.huluwa.exception.PlaceOrderFailedException;
import com.huluwa.model.RequestMessage;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 交易接口
 */
public interface TransactionService {


    /**
     * 下单请求
     * @param r 商户必须的请求信息.
     */
    @Deprecated
    String transaction(RequestMessage r) throws IOException;

    // 固定，已配置

    /**
     * 发起一笔可使用网页完成的支付
     * @param outTradeNo 商户系统内部的订单号,32个字符内、可包含字母, 确保在商户系统唯一 {@link OrderPaySuccessEvent#getOrderId()}
     * @param body 商品详情
     * @param amount 金额，单位是元；最多保留两位；不支持一元以下的支付
     * @return 一个网页URL，可引导用户完成支付
     * @throws PlaceOrderFailedException 下单失败异常
     */
    String htmlPay(String outTradeNo,String body,BigDecimal amount) throws PlaceOrderFailedException, IOException;


}
