package com.huluwa.service;


import com.huluwa.config.PaymentSpringConfig;
import com.huluwa.model.RequestMessage;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;

@WebAppConfiguration
@ContextConfiguration(classes = PaymentSpringConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    /**
     * 测试环境：http://pay-wx.join51.com/
     * http://pay-wx.join51.com/oft/acquirePlatform/api/transfer.html
     * <p>
     * 测试商户号：000000010000000001
     * <p>
     * 测试密钥：55be454630e847d7815c2c2d3bc59c0d
     * <p>
     * 仅支持网关支付：gateway
     *
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
        rm.setURLPrefix("http://www.baidu.com");
        rm.setOutTradeNo("0010");
        //测试时只接受网关
        rm.setChannel("gateway");
        transactionService.transaction(rm);
    }

    /**
     * 浙江手聚文化创意有限公司  测试
     * 登录网址： http://pay-slb-636362782.ap-northeast-2.elb.amazonaws.com:43251/acquire

     商户名称： 浙江手聚文化创意有限公司

     商户结算人名称： 浙江手聚文化创意有限公司

     商户号： 000000020000000002

     初始密码： 667788

     商户技术秘钥： 64050e37ffb24b2585d7dd16d77ac423

     备案手机： 18023011077

     备案邮箱： shouju778899@sohu.com

     注意事项： 请初始登录修改用户密码，谢谢！
     */
    @Test
    public void wxQrTest() throws IOException {
        //写一个下单请求 ,以商户身份
        RequestMessage rm = new RequestMessage();
        //范围是10 -  3000元
        rm.setAmount(new BigDecimal("10"));
        rm.setBody("测试的商品");
        rm.setKey("64050e37ffb24b2585d7dd16d77ac423");
        rm.setMchId("000000020000000002");
        //随便写的
        rm.setURLPrefix("http://www.baidu.com");
        int i = RandomUtils.nextInt();
        rm.setOutTradeNo("ms"+i);
        //支付方式
        rm.setChannel("wxPubQR");

        String transaction = transactionService.wxQrPay("ms"+i, "测试的商品", new BigDecimal("10"));
        System.out.println(transaction);
    }

}
