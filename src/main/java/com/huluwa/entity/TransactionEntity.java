package com.huluwa.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易接口参数实体
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionEntity {

    /**
     * 交易类型  pay.submit
     */
    private String tradeType;

    /**
     * 版本号，1.7
     */
    private String version;

    /**
     * 支付类型
     */
    private String channel;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 签名
     */
    private String sign;

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
     * 订单创建时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     */
    private String timePaid;

    /**
     * 订单失效时间，格式同上,不传默认是上游的渠道失效时间，传失效时间不能低于创建时间5分钟以下
     */
    private String timeExpire;

    /**
     * 结算周期,0(D0)1(T1)默认结算周期是T1
     */
    private int settleCycle;

    /**
     * 银行代码；支付类型为gateway时该参数有效；是否必填根据上游通道而定；接入时请与平台确认并获取支持的银行代码
     */
    private String bankCode;

    /**
     * D：储蓄卡；C：信用卡
     支付类型为gateway、qpay时该参数有效；是否必填根据上游通道而定；接入时请与平台确认
     */
    private String cardType;

    /**
     *  银行卡验证信息支付类型为qpay时该参数有效；是否必填根据上游通道而定；接入时请与平台确认。
     Json格式的字符串；字段包含：
     bankCardNo:银行卡号,必填,
     customerName:姓名,必填,
     phoneNo:手机号,必填,
     cerType:证件类型,必填（01:身份证，99：其他）,
     cerNo:证件号,必填,
     validDate:信用卡有效期,格式：YYMM,信用卡必填,
     cvn2:信用卡CVV2,信用卡必填
     例子：
     {\"bankCardNo\":\"62251222333344\",\"customerName\":\"abcd\",\"phoneNo\":\"13510727311\",\"cerType\":\"01\",\"cerNo\":\"445124199011140321\"",\"validDate\":\"2209\",\"cvn2\":\"4311\"}
     */
    private String payCardInfo;
    /**
     * 终端类型 1：PC；2：手机
     * 支付类型为gateway时该参数有效；是否必填根据上游通道而定；接入时请与平台确认
     */
    private String accessType;

    /**
     * 支付完成后结果通知url；参数参考交易详细查询;
     */
    private String notifyUrl;

    /**
     * 支付成功跳转路径；form表单形式提交商户后台；参数参考交易详细查询;
     以下支付类型该参数有效：
     1.wxPub
     2.wxH5
     3.qpay
     4.jdPay
     5.gateway
     */
    private String callbackUrl;
}
