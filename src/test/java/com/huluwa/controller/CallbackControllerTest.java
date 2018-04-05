package com.huluwa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huluwa.config.PaymentSpringConfig;
import com.huluwa.event.OrderPaySuccessEvent;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {PaymentSpringConfig.class,CallbackControllerTest.Config.class})
@WebAppConfiguration
public class CallbackControllerTest extends SpringWebTest {

    static OrderPaySuccessEvent lastEvent = null;

    ///
    @Configuration
    public static class Config {
        @EventListener(OrderPaySuccessEvent.class)
        public void getIt(OrderPaySuccessEvent event) {
            lastEvent = event;
        }
    }

    @Test
    public void transferResult() throws Exception {
        testSuccessWithOrder("0aab2");
        testSuccessWithOrder("0a10s211");
    }

    private void testSuccessWithOrder(String orderId) throws Exception {
        lastEvent = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode infoEntity = mapper.readTree(new ClassPathResource("/success_with_" + orderId + ".json").getFile());
        JsonNode requestEntity = infoEntity.get("request");

        mockMvc.perform(post("/huluwa/transfer")
                .contentType(requestEntity.get("headers").get("Content-type").get(0).asText())
                .content(requestEntity.get("content").asText())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"))
        ;
        Assert.assertNotNull("确保收到了成功事件", lastEvent);
        Assert.assertEquals("收到的成功支付事件id是正确的", orderId, lastEvent.getOrderId());
    }
}