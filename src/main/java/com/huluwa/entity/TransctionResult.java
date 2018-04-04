package com.huluwa.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易指令响应参数实体
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransctionResult {

    /**
     *返回状态码	returnCode	是	String(1)	0：成功、1：失败
     此字段是通信标识，非交易标识，交易是否成功需要查看resultCode来判断
     */
    private String returnCode;

    /**
     * 返回信息	returnMsg	否	String(128)	返回信息，如非空，为错误原因
     签名失败
     参数格式校验错误
     */
    private String returnMsg;

    /**
     * 除付款码支付外其他支付类型均有返回;
     包括以下取值：
     01：支付url；
     02：二维码url；
     03：自动跳转html；
     04：支付渠道json格式支付参数
     05：快捷短信验证（为该值时，需要调用短信接口以及订单确认接口）
     06：快捷确认支付（为该值时，快捷下单时，会同时发短信验证码到客户，没收到短信情况下，可调用短信接口，重新发送；后续需要调用订单确认接口完成支付）
     */
    private String payCodeWay;

    /**
     * 根据payCodeWay值进行相对的处理
     注意：网关可能存在payCodeWay为01，03的情况，为兼容不同上游，请务必处理两种情况；payCodeWay为05时，该字段无值。
     */
    private String payCode;

    /**
     * 签名
     */
    private String sign;

    /**
     * 业务结果状态
     0：成功、1：失败
     */
    private String resultCode;

    /**
     * 商户系统内部的订单号,32个字符内、可包含字母, 确保在商户系统唯一
     */
    private String outTradeNo;

    /**
     * 上游渠道返回的订单号
     */
    private String outChannelNo;

    /**
     * 交易金额；单位为元，小数两位
     */
    private BigDecimal amount;

    /**
     * 交易时间,格式为yyyyMMddHHmmss
     */
    private String transTime;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误描述
     */
    private String errCodeDes;
}
