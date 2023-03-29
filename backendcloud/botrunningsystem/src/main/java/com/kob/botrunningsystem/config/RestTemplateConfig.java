package com.kob.botrunningsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author xzt
 * @version 1.0
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate getRestTemplate() {
        // RestTemplate 可以在两个spring boot之间进行通讯
        return new RestTemplate();
    }
}
