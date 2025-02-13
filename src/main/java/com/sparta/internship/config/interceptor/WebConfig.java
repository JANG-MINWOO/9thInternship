package com.sparta.internship.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sparta.internship.config.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

// Spring Security 와 별개로 Spring MVC 에서 동작하는 Interceptor 설정을 담당하는 클래스
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final JwtUtil jwtUtil;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestInterceptor(jwtUtil))
			.addPathPatterns("/restricted-api/**");
	}
}
