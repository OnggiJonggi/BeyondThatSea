package com.hugme.plz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hugme.plz.common.interceptor.MemberInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MemberInterceptor())
			.addPathPatterns("/memeber/*","/videoCall/*")
			.excludePathPatterns("insertExcludeHere");
	}
}
