package com.hugme.plz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.openvidu.java.client.OpenVidu;

@Configuration
@PropertySource("classpath:config/openvidu-config.properties")
public class OpenViduConfig {
	// 192.168.150.27
    @Value("${openvidu.url}")
    private String URL;
    
    // 192.168.0.11
    @Value("${openvidu.url2}")
    private String URL2;
    
    @Value("${openvidu.secret}")
    private String SECRET;

    @Bean
    public OpenVidu openViduBean() {
        try {            
            return new OpenVidu(URL2, SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OpenVidu 초기화 실패", e);
        }
    }
}
