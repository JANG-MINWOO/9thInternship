package com.sparta.internship.domain.token.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.internship.config.jwt.JwtUtil;
import com.sparta.internship.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtUtil jwtUtil;

	public Map<String, Object> decodeToken(HttpServletRequest request) {
		// 쿠키에서 JWT 가져오기
		String tokenValue = jwtUtil.getTokenFromHeader(request);
		if (tokenValue == null) {
			throw new IllegalArgumentException("JWT 토큰이 요청에 포함되지 않았습니다.");
		}

		Claims claims = jwtUtil.extractClaims(tokenValue);

		// JWT Base64 디코딩
		String[] tokenParts = tokenValue.split("\\.");
		if (tokenParts.length < 2) {
			throw new IllegalArgumentException("유효하지 않은 JWT 형식입니다.");
		}
		String header = new String(Base64.getDecoder().decode(tokenParts[0]));
		String payload = new String(Base64.getDecoder().decode(tokenParts[1]));

		log.info("JWT Header: {}", header);
		log.info("JWT Payload: {}", payload);

		return Map.of(
			"header", header,
			"payload", payload,
			"claims", claims
		);
	}

	public Map<String, String> refreshAccessToken(HttpServletRequest request) {
		// Refresh Token 을 쿠키에서 가져오기
		String refreshToken = jwtUtil.getRefreshTokenFromCookie(request);
		if (refreshToken == null) {
			log.warn("Refresh Token 이 요청에 없음");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token 이 필요합니다.");
		}

		// Refresh Token 유효성 검사
		String strippedToken = jwtUtil.substringToken(refreshToken);
		if (!jwtUtil.validateRefreshToken(strippedToken)) {
			log.warn("유효하지 않은 Refresh Token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token");
		}

		// Refresh Token 이 유효하면 새로운 Access Token 발급
		Claims claims = jwtUtil.extractClaims(strippedToken);
		String newAccessToken = jwtUtil.createAccessToken(
			Long.valueOf(claims.getSubject()),
			claims.get("username", String.class),
			UserRole.USER
		);

		log.info("새로운 Access Token 발급 완료");
		return Map.of("accessToken", newAccessToken);
	}
}
