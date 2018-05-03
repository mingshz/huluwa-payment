package com.huluwa.model;

import lombok.Data;

/**
 * 快捷支付, 银行卡信息
 * 支付类型为qpay时该参数有效；是否必填根据上游通道而定；接入时请与平台确认。
 * Json格式的字符串；字段包含：
 * bankCardNo:银行卡号,必填,
 * customerName:姓名,必填,
 * phoneNo:手机号,必填,
 * cerType:证件类型,必填（01:身份证，99：其他）,
 * cerNo:证件号,必填,
 * validDate:信用卡有效期,格式：YYMM,信用卡必填,
 * cvn2:信用卡CVV2,信用卡必填
 * 例子：
 * {\"bankCardNo\":\"62251222333344\",\"customerName\":\"abcd\",\"phoneNo\":\"13510727311\",\"cerType\":\"01\",\"cerNo\":\"445124199011140321\"",\"validDate\":\"2209\",\"cvn2\":\"4311\"}
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayCardInfo {

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 姓名
     */
    private String customerName;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 证件类型  01:身份证  99:其他
     */
    private String cerType;

    /**
     * 证件号
     */
    private String cerNo;

    /**
     * validDate:信用卡有效期,格式：YYMM,信用卡必填
     */
    private String validDate;

    /**
     * cvn2:信用卡CVn2,信用卡必填
     */
    private String cvn2;

}
