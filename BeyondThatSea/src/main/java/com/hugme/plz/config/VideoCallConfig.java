package com.hugme.plz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:config/dailyco.properties")
public class VideoCallConfig {
    @Value("${daily.api.url}")
    private String apiUrl;
    
    @Value("${daily.api.key}")
    private String apiKey;
    
    @Bean
    public WebClient dailyWebClient() {
        return WebClient.builder()
            .baseUrl(apiUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
    }
}
