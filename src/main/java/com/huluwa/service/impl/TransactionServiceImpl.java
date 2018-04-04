package com.huluwa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huluwa.entity.TransactionEntity;
import com.huluwa.exception.PlaceOrderFailedException;
import com.huluwa.model.RequestMessage;
import com.huluwa.service.TransactionService;
import com.huluwa.util.HttpsClientUtil;
import com.huluwa.util.SignUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter fotmatterYear = DateTimeFormatter.ofPattern("yyyyMMdd");
    ObjectMapper objectMapper = new ObjectMapper();

    private final String sendUrl;

    public TransactionServiceImpl(Environment environment) {
        this.sendUrl = environment.getProperty("huluwa.transferUrl", "http://pay-wx.join51.com/oft/acquirePlatform/api/transfer.html");
    }

    private static Log log = LogFactory.getLog(TransactionServiceImpl.class);

    /**
     * 下单请求
     *
     * @param r 需要商户发过来的请求信息
     * @throws IOException
     */
    public String transaction(RequestMessage r) throws IOException {
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
        transactionEntity.setAmount(r.getAmount());
        //商户方商品id
        transactionEntity.setMchId(r.getMchId());
        //交易方式
        transactionEntity.setChannel(r.getChannel());
        //异步回调地址
        transactionEntity.setNotifyUrl(r.getURLprefix() + "/huluwa/transfer");

        //转成json
        String s = objectMapper.writeValueAsString(transactionEntity);
        //转成map
        Map<String, String> map = objectMapper.readValue(s, Map.class);

        //调用demo中的拼接字符串方法
        String toSign = SignUtil.createLinkString(map);
        // 生成签名sign
        String sign = SignUtil.genSign(key, toSign);
        map.put("sign", sign);
        //转换成json串
        String postStr = objectMapper.writeValueAsString(map);
        log.info("发送的请求信息" + postStr);

        String returnStr = HttpsClientUtil.sendRequest(sendUrl, postStr, "application/json");
        Map returnMap = objectMapper.readValue(returnStr, Map.class);

        String returnCode = (String)returnMap.get("returnCode");
        if (returnCode.equals("1")) {
            throw new PlaceOrderFailedException("请求失败:"+returnStr);
        }
        // 验签
        if (!SignUtil.validSign(returnMap, key)) {
            throw new PlaceOrderFailedException("验签错误");
        }
        String resultCode = (String)returnMap.get("resultCode");
        if (resultCode.equals("0")) {
            String payCode = (String)returnMap.get("payCode");
            String payCodeWay = (String)returnMap.get("payCodeWay");
            log.info("支付地址" + payCode);
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

}