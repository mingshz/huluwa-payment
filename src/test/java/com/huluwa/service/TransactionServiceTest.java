package com.huluwa.service;


import com.huluwa.config.ServiceConfig;
import com.huluwa.model.RequestMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;

@WebAppConfiguration
@ContextConfiguration(classes = ServiceConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    /**
     * 测试环境：http://pay-wx.join51.com/
     http://pay-wx.join51.com/oft/acquirePlatform/api/transfer.html

     测试商户号：000000010000000001

     测试密钥：55be454630e847d7815c2c2d3bc59c0d

     仅支持网关支付：gateway

     * @throws IOException
     */
    @Test
    public void go() throws IOException {
        //写一个下单请求 ,以商户身份
        RequestMessage rm = new RequestMessage();
        rm.setAmount(new BigDecimal("100.23"));
        rm.setBody("测试的商品");
        rm.setKey("55be454630e847d7815c2c2d3bc59c0d");
        rm.setMchId("000000010000000001");
        //随便写的
        rm.setURLprefix("http://www.baidu.com");
        rm.setSettleCycle(0);
        rm.setOutTradeNo("002");
        //测试时只接受网关
        rm.setChannel("gateway");
        transactionService.transaction(rm);
    }
}
