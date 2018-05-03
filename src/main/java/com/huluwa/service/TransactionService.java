package com.huluwa.service;

import com.huluwa.exception.PlaceOrderFailedException;
import com.huluwa.model.CardType;
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
     * @param outTradeNo 商户系统内部的订单号,32个字符内、可包含字母, 确保在商户系统唯一
     * @param body 商品详情
     * @param amount 金额，单位是元；最多保留两位；不支持一元以下的支付
     * @return 一个网页URL，可引导用户完成支付
     * @throws PlaceOrderFailedException 下单失败异常
     */
    String htmlPay(String outTradeNo,String body,BigDecimal amount) throws PlaceOrderFailedException, IOException;

    /**
     * 快捷支付
     * @param cardNo  银行卡号
     * @param customerName 银行卡姓名
     * @param phoneNo 预留手机号
     * @param cardType 卡的类型
     * @param validDate 信用卡有效期 YYMM
     * @param cvn2 信用卡 cvn2号 可以为null
     * @param cvv2 信用卡 cvv2号 可以为null
     * @param idNumber 身份证号
     * @return
     */
    String qpay(String outTradeNo, String body, BigDecimal amount,String cardNo,String customerName,String phoneNo,CardType cardType,String validDate,String cvn2,String cvv2,String  idNumber) throws IOException;

    /**
     * 微信扫码支付
     * @param outTradeNo 商户系统内部的订单号,32个字符内、可包含字母, 确保在商户系统唯一
     * @param body 商品详情
     * @param amount 金额，单位是元；最多保留两位；不支持一元以下的支付
     * @return
     * @throws PlaceOrderFailedException
     * @throws IOException
     */
    String wxQrPay(String outTradeNo,String body,BigDecimal amount) throws PlaceOrderFailedException, IOException;
}
