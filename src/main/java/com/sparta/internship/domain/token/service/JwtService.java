package com.sparta.internship.domain.token.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sparta.internship.config.jwt.JwtUtil;

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
		String tokenValue = jwtUtil.getTokenFromRequest(request);
		if (tokenValue == null) {
			throw new IllegalArgumentException("JWT 토큰이 요청에 포함되지 않았습니다.");
		}

		String strippedToken = jwtUtil.substringToken(tokenValue);
		Claims claims = jwtUtil.extractClaims(strippedToken);

		// JWT Base64 디코딩
		String[] tokenParts = strippedToken.split("\\.");
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
}
