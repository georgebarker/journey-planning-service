package com.georgebarker.journeyplanningservice.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RestTemplateConfigTest {

    @Autowired
    RestTemplateConfig restTemplateConfig;
    
    @Test
    public void testRestTemplate() {
        RestTemplate restTemplate = restTemplateConfig.restTemplate();
        assertNotNull(restTemplate);
    }
}
