package com.huluwa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({
        "com.huluwa.service"
})
@Import({MvcConfig.class})
public class ServiceConfig {
}
