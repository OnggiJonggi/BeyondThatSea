package com.hugme.plz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//시큐리티 기본 로그인 화면 비활성화
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BeyondThatSeaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeyondThatSeaApplication.class, args);
	}

}
