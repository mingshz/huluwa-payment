package com.huluwa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huluwa.config.PaymentSpringConfig;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@ContextConfiguration(classes = PaymentSpringConfig.class)
@WebAppConfiguration
public class CallbackControllerTest extends SpringWebTest {

    ///

    @Test
    public void transferResult() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode infoEntity = mapper.readTree(new ClassPathResource("/success.json").getFile());
        JsonNode requestEntity = infoEntity.get("request");

        mockMvc.perform(post("/huluwa/transfer")
                .contentType(requestEntity.get("headers").get("Content-type").asText())
                .content(requestEntity.get("content").asText())
        )
                .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("SUCCESS"))
        ;


    }
}