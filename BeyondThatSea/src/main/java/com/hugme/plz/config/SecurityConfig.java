package com.hugme.plz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//해당 클래스가 설정 파일임을 나타나는 어노테이션
@Configuration
public class SecurityConfig {
	//등록하려는 bean : @Bean어노테이쑌
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
