package com.sparta.internship.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Security 와 별개로 Spring MVC 에서 동작하는 Interceptor 설정을 담당하는 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestInterceptor())
			.addPathPatterns("/restricted-api/**");
	}
}
