package com.huluwa.jackson;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huluwa.entity.TransactionEntity;
import com.huluwa.util.SignUtil;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class JacksonTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 测试 生成的json字符串是否符合预期
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccessType("测试");
        transactionEntity.setAmount(new BigDecimal("100.28").toString());
        transactionEntity.setBody("测试商品");
        String s = objectMapper.writeValueAsString(transactionEntity);
        System.out.println(s);

        String key = "827djez9kejfu7m28ems4jehrek1m";
        JSONObject map = objectMapper.readValue(s, JSONObject.class);
        String toSign = SignUtil.createLinkString(map);
        // 生成签名sign
        String sign = SignUtil.genSign(key, toSign);
        map.put("sign", sign);
        //转换成json串
        String sentMessage = objectMapper.writeValueAsString(map);
        System.out.println(sentMessage);
    }
}
