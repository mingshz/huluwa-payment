package com.huluwa.service;


import com.huluwa.config.PaymentSpringConfig;
import com.huluwa.model.PayCardInfo;
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

    @Test
    public void goGoGo() throws IOException {
        //写一个下单请求 ,以商户身份
        RequestMessage rm = new RequestMessage();
        rm.setAmount(new BigDecimal("1"));
        rm.setBody("测试的商品");
        rm.setKey("55be454630e847d7815c2c2d3bc59c0d");
        rm.setMchId("000000010000000001");
        //随便写的
        rm.setURLPrefix("http://dhs.mingshz.com/huluwa");
        rm.setOutTradeNo("0a10s211");
        //测试时只接受网关
        rm.setChannel("gateway");
        String transaction = transactionService.transaction(rm);
        System.out.println(transaction);
    }


    /**
     * 鄂州市灵动网络科技有限公司  测试
     * 登录网址： http://pay-slb-636362782.ap-northeast-2.elb.amazonaws.com:43251/acquire
     商户名称： 鄂州市灵动网络科技有限公司
     商户结算人名称： 鄂州市灵动网络科技有限公司
     商户号： 000000020000000001
     初始密码： 667788
     商户技术秘钥： e375e8432d044f6ebb691c43c307e682
     备案手机： 15308684781
     备案邮箱： 15308684781@163.com
     注意事项： 请初始登录修改用户密码，谢谢！
     */
    @Test
    public void ezldTest() throws IOException {
        //写一个下单请求 ,以商户身份
        RequestMessage rm = new RequestMessage();
        rm.setAmount(new BigDecimal("1"));
        rm.setBody("测试的商品");
        rm.setKey("64050e37ffb24b2585d7dd16d77ac423");
        rm.setMchId("000000020000000002");
        //随便写的
        rm.setURLPrefix("http://www.baidu.com");
        rm.setOutTradeNo("test2");
        //支付方式
        rm.setChannel("qpay");

        // 银行卡信息
        PayCardInfo payCardInfo = new PayCardInfo();
        payCardInfo.setBankCardNo("6225884519945847");
        payCardInfo.setCustomerName("李雪峰");
        payCardInfo.setPhoneNo("18804514144");
        //证件类型  身份证 01
        payCardInfo.setCerType("01");
        payCardInfo.setCerNo("230103199207103655");
        rm.setPayCardInfo(payCardInfo);
        String transaction = transactionService.transaction(rm);
        System.out.println(transaction);
    }

}
