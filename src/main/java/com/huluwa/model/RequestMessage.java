package com.huluwa.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易请求接受参数的实体
 */
@Data
public class RequestMessage {

    /**
     * 密钥
     */
    private String key;
    /**
     * 支付类型
     weixin	微信
     alipay	支付宝
     jd	京东
     qpay	快捷支付
     gateway	网关支付
     QQPay	QQ钱包
     quickpass	银联
     */
    private String channel;

    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商品描述,商品或支付单简要描述
     */
    private String body;

    /**
     * 商品订单号,商户系统内部的订单号,32个字符内、可包含字母, 确保在商户系统唯一
     */
    private String outTradeNo;
    /**
     * 单位为元，小数两位
     */
    private BigDecimal amount;
    /**
     * 结算周期,0(D0)1(T1)默认结算周期是T1
     */
    private int settleCycle;

    /**
     * 回调地址前缀
     */
    private String URLprefix;


}
