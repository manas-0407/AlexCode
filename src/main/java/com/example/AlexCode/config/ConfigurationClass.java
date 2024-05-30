package com.example.AlexCode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigurationClass {

    /*
    To send HTTP post request
     */

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
