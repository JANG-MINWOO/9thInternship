package com.sparta.internship.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@SecurityScheme( // API 호출 시 JWT 토큰을 자동으로 포함하게 설정하는 버튼 생성
	name = "BearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
@SecurityRequirement(name = "BearerAuth")
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Internship Project API")
				.description("JWT 기반 인증 시스템 API 문서")
				.version("1.0.0"));
	}
}
