package com.sparta.internship.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sparta.internship.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;

class JwtUtilTest {

	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		// JwtUtil 인스턴스 초기화
		jwtUtil = new JwtUtil();

		jwtUtil.setSecretKey("7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg=="); // 최소 32바이트
		jwtUtil.init(); // 초기화 메서드 호출
	}

	@Test
	@DisplayName("JWT 토큰 - 생성 테스트")
	void testCreateToken() {
		// Given
		Long id = 1L;
		String username = "testuser";
		UserRole userRole = UserRole.USER;

		// When
		String token = jwtUtil.createToken(id, username, userRole);

		// Then
		assertThat(token).isNotNull();
		System.out.println("Generated Token: " + token);
	}

	@Test
	@DisplayName("JWT 토큰 - 사용자 이름 추출 테스트")
	void testExtractUsernameFromToken() {
		// Given
		Long id = 1L;
		String username = "testuser";
		UserRole userRole = UserRole.USER;
		String token = jwtUtil.createToken(id, username, userRole);
		String substringToken = jwtUtil.substringToken(token);

		// When
		Claims info = jwtUtil.extractClaims(substringToken);
		String extractedUsername = info.get("username", String.class);

		// Then
		assertThat(extractedUsername).isEqualTo(username);
		System.out.println("username: " + extractedUsername);
	}

	@Test
	@DisplayName("JWT 토큰 - 검증 테스트")
	void testValidateToken() {
		// Given
		Long id = 1L;
		String username = "testuser";
		UserRole userRole = UserRole.USER;
		String token = jwtUtil.createToken(id, username, userRole);
		String substringToken = jwtUtil.substringToken(token);

		// When
		boolean isValid = jwtUtil.validateToken(substringToken);

		// Then
		assertThat(isValid).isTrue();
	}
}