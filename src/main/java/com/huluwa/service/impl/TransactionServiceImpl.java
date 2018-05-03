package com.huluwa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huluwa.entity.TransactionEntity;
import com.huluwa.exception.PlaceOrderFailedException;
import com.huluwa.exception.RequestDataException;
import com.huluwa.model.CardType;
import com.huluwa.model.PayCardInfo;
import com.huluwa.model.RequestMessage;
import com.huluwa.service.TransactionService;
import com.huluwa.util.HttpsClientUtil;
import com.huluwa.util.SignUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@CommonsLog
public class TransactionServiceImpl implements TransactionService {

    ObjectMapper objectMapper = new ObjectMapper();

    private final String sendUrl;
    private final String key;
    private final String mchId;
    //回调地址前缀
    private final String URLPrefix;

    @Autowired
    public TransactionServiceImpl(Environment environment) {
        this.sendUrl = environment.getProperty("huluwa.transferUrl", "http://pay-slb-636362782.ap-northeast-2.elb.amazonaws.com:43251/acquire/acquirePlatform/api/transfer.html");
        this.key = environment.getProperty("huluwa.mKey", "64050e37ffb24b2585d7dd16d77ac423");
        this.mchId = environment.getProperty("huluwa.mchId", "000000020000000002");
        this.URLPrefix = environment.getProperty("huluwa.URLPrefix","http://dhs.mingshz.com/huluwa/transfer");
    }

    /**
     * 下单请求
     *
     * @param r 需要商户发过来的请求信息
     * @throws IOException
     */
    public String transaction(RequestMessage r) throws IOException {
        BigDecimal amount = r.getAmount();
        double v = amount.doubleValue();
        if(v < 1.0){
            throw new PlaceOrderFailedException("请求失败:金额最小1元");
        }
        //商户密钥
        String key = r.getKey();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTradeType("pay.submit");
        transactionEntity.setVersion("1.7");
        //当天到账
        transactionEntity.setSettleCycle(0);
        //商品订单号
        transactionEntity.setOutTradeNo(r.getOutTradeNo());
        //商品描述
        transactionEntity.setBody(r.getBody());
        //金额
        transactionEntity.setAmount(r.getAmount().toString());
        //商户方商品id
        transactionEntity.setMchId(r.getMchId());
        //交易方式
        transactionEntity.setChannel(r.getChannel());
        //异步回调地址
        transactionEntity.setNotifyUrl(r.getURLPrefix());

        //如果是快捷支付需要银行卡信息
        if("qpay".equals(r.getChannel())){
            transactionEntity.setPayCardInfo(r.getPayCardInfo());
        }
        //转成json
        String s = objectMapper.writeValueAsString(transactionEntity);
        //转成map
        JSONObject reqStr = JSON.parseObject(s);
        //调用demo中的拼接字符串方法
        String toSign = SignUtil.createLinkString(reqStr);
        // 生成签名sign
        String sign = SignUtil.genSign(key, toSign);
        reqStr.put("sign", sign);
        //转换成json串
        String postStr = reqStr.toJSONString();
        log.info("发送的请求信息" + postStr);

        String returnStr = HttpsClientUtil.sendRequest(sendUrl, postStr);
        log.info("返回的响应信息" + returnStr);
        JSONObject returnMap = JSON.parseObject(returnStr);

        String returnCode = returnMap.getString("returnCode");
        if (returnCode.equals("1")) {
            throw new PlaceOrderFailedException("请求失败:"+returnStr);
        }

        String resultCode = returnMap.getString("resultCode");
        if (resultCode == null || resultCode.equals("0")) {
            String payCode = returnMap.getString("payCode");
            String payCodeWay = returnMap.getString("payCodeWay");
            if ("01".equals(payCodeWay)) {// url
                return "redirect:" + payCode;
            } else if ("02".equals(payCodeWay)) {// 二维码
                return payCode;
            } else if ("04".equals(payCodeWay)) {// JSON
                return payCode;
            } else if ("03".equals(payCodeWay)) {// form
                throw new PlaceOrderFailedException("意外的支付方式");
            } else if ("05".equals(payCodeWay)) {
                throw new PlaceOrderFailedException("意外的支付方式");
            }
            throw new PlaceOrderFailedException("意外的支付方式");
        } else {
            throw new PlaceOrderFailedException("业务结果状态:失败");
        }
    }

    private RequestMessage encapsulation(String outTradeNo, String body, BigDecimal amount,String channel){
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setOutTradeNo(outTradeNo);
        requestMessage.setBody(body);
        requestMessage.setAmount(amount);
        requestMessage.setChannel(channel);
        requestMessage.setMchId(mchId);
        requestMessage.setKey(key);
        requestMessage.setURLPrefix(URLPrefix);
        return requestMessage;
    }

    @Override
    public String htmlPay(String outTradeNo, String body, BigDecimal amount) throws PlaceOrderFailedException, IOException {
        RequestMessage requestMessage = encapsulation(outTradeNo, body, amount, "gateway");
        return transaction(requestMessage);
    }



    @Override
    public String qpay(String outTradeNo, String body, BigDecimal amount,String cardNo, String customerName, String phoneNo, CardType cardType, String validDate,String cvn2, String cvv2, String idNumber) throws IOException {
        RequestMessage requestMessage = encapsulation(outTradeNo, body, amount, "qpay");
        PayCardInfo payCardInfo = new PayCardInfo();
        payCardInfo.setBankCardNo(cardNo);
        payCardInfo.setCustomerName(customerName);
        payCardInfo.setPhoneNo(phoneNo);
        if(cardType.equals(CardType.CASH_CARD)){
            //储蓄卡
        }else{
            //信用卡
            payCardInfo.setValidDate(validDate);
            if(StringUtils.isNotEmpty(cvn2)){
                payCardInfo.setCvn2(cvn2);
            }else{
                throw new RequestDataException("信用卡信息错误");
            }
        }
        //身份证
        payCardInfo.setCerType("01");
        payCardInfo.setCerNo(idNumber);
        requestMessage.setPayCardInfo(payCardInfo);
        return transaction(requestMessage);
    }

    @Override
    public String wxQrPay(String outTradeNo, String body, BigDecimal amount) throws PlaceOrderFailedException, IOException {
        RequestMessage requestMessage = encapsulation(outTradeNo, body, amount, "wxPubQR");
        return transaction(requestMessage);
    }

}
