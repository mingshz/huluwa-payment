package com.huluwa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * 载入该配置可以获得葫芦娃支付接入功能
 * 提供
 * <ul>
 *     <li>{@link com.huluwa.service.TransactionService} spring bean</li>
 *     <li>spring 事件</li>
 * </ul>
 * 需要
 * <ul>
 *
 * </ul>
 *
 * @author CJ
 */
@Configuration
@ComponentScan({
        "com.huluwa.service"
})
@PropertySource({"classpath:/pay_message.properties"})
@Import({MvcConfig.class})
public class PaymentSpringConfig {
}
