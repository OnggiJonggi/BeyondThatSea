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
    
    @Value("${openvidu.secret}")
    private String SECRET;
	
    @Bean
    public OpenVidu openViduBean() {
    	//아래 코드는 SSL인증을 비활성화한다.
    	//실제 환경에서 이따구로 싸질러놓으면 서버 털린다.
    	System.setProperty("openvidu.disable.ssl.verification", "true");
    	
        return new OpenVidu(URL, SECRET);
    }
}