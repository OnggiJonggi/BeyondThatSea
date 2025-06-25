package com.hugme.plz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.openvidu.java.client.OpenVidu;

@Configuration
@PropertySource("classpath:config/openvidu-config.properties")
public class OpenViduConfig {
    @Value("${openvidu.url}")
    private String URL;
    
    @Value("${openvidu.urllh}")
    private String URLLH;
    
    @Value("${openvidu.secret}")
    private String SECRET;
	
    @Bean
    public OpenVidu openViduBean() {
        return new OpenVidu(URL, SECRET);
    }
}