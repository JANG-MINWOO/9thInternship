package com.sparta.internship.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.internship.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@SpringBootTest
class JwtUtilTest {

	private static final Logger log = LoggerFactory.getLogger(JwtUtilTest.class);

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
		String token = jwtUtil.createAccessToken(id, username, userRole);

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
		String token = jwtUtil.createAccessToken(id, username, userRole);
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
		String token = jwtUtil.createAccessToken(id, username, userRole);
		String substringToken = jwtUtil.substringToken(token);

		// When
		boolean isValid = jwtUtil.validateToken(substringToken);

		// Then
		assertThat(isValid).isTrue();
	}

	@Test
	@DisplayName("JWT 토큰 - 서명 검증 테스트")
	void testJwtSignatureValidation() {
		// Given
		Long id = 1L;
		String username = "testuser";
		UserRole userRole = UserRole.USER;

		// When
		String token = jwtUtil.createAccessToken(id, username, userRole);
		String strippedToken = jwtUtil.substringToken(token);

		// Then
		try {
			Jwts.parserBuilder()
				.setSigningKey(jwtUtil.getSigningKey())
				.build()
				.parseClaimsJws(strippedToken);

			log.info("JWT 서명 검증 성공");
			assertThat(true).isTrue();
		} catch (Exception e) {
			log.error("JWT 서명 검증 실패: {}", e.getMessage());
			fail("JWT 서명 검증 실패");
		}
	}

	@Test
	@DisplayName("Refresh Token - 생성 테스트")
	void testCreateRefreshToken() {
		// Given
		Long id = 1L;
		String username = "testuser";

		// When
		String refreshToken = jwtUtil.createRefreshToken(id, username);

		// Then
		assertThat(refreshToken).isNotNull();
		log.info("Refresh Token 발급 성공: {}", refreshToken);
	}

	@Test
	@DisplayName("Refresh Token - 검증 테스트")
	void testValidateRefreshToken() {
		// Given
		Long id = 1L;
		String username = "testuser";
		String refreshToken = jwtUtil.createRefreshToken(id, username);
		String strippedToken = jwtUtil.substringToken(refreshToken);

		// When
		boolean isValid = jwtUtil.validateRefreshToken(strippedToken);

		// Then
		assertThat(isValid).isTrue();
		log.info("Refresh Token 검증 성공");
	}

	@Test
	@DisplayName("Refresh Token - Access Token 만료 후 R/T 검증 테스트")
	void testAccessTokenExpiredAndRefreshTokenValid() throws InterruptedException {
		// Given
		Long id = 1L;
		String username = "testuser";
		String accessToken = jwtUtil.createShortLivedAccessToken(id, username, UserRole.USER);
		String refreshToken = jwtUtil.createRefreshToken(id, username);

		Thread.sleep(3000);

		boolean isAccessTokenValid;
		boolean isRefreshTokenValid;

		try {
			isAccessTokenValid = jwtUtil.validateToken(jwtUtil.substringToken(accessToken));
		} catch (ExpiredJwtException e) {
			isAccessTokenValid = false;
			log.info("Access Token 이 만료됨");
		}

		isRefreshTokenValid = jwtUtil.validateRefreshToken(jwtUtil.substringToken(refreshToken));

		// Then
		assertThat(isAccessTokenValid).isFalse();
		assertThat(isRefreshTokenValid).isTrue();
		log.info("Access Token 만료 후 Refresh Token 검증 성공");
	}

	@Test
	@DisplayName("Refresh Token - 잘못된 토큰이므로 검증 실패")
	void testInvalidRefreshToken() {
		// Given
		String invalidToken = "invalidToken";

		// When
		boolean isValid = jwtUtil.validateRefreshToken(invalidToken);

		// Then
		assertThat(isValid).isFalse();
		log.info("잘못된 Refresh Token 검증 실패");
	}
}