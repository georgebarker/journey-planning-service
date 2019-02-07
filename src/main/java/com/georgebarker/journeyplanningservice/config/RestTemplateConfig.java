package com.georgebarker.journeyplanningservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * I am a class that allows RestTemplate to be @Autowired and used as a Bean
 * (Singleton). This is useful because Rest Templates are thread-safe and so
 * this can be used multiple times by multiple connections. This means that
 * loads of Rest Templates won't be created unnecessarily, too.
 * 
 * @author georgebarker
 *
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
