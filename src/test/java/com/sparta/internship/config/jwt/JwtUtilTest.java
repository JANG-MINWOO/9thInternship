package com.sparta.internship.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.internship.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;

@SpringBootTest
class JwtUtilTest {

	@Autowired
	private JwtUtil jwtUtil;

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