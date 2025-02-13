package com.sparta.internship.domain.token.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.token.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class JwtController {

	private final JwtService jwtService;

	@GetMapping("/decode")
	public ResponseEntity<Map<String, Object>> decodeToken(HttpServletRequest request) {
		try {
			Map<String, Object> decodedToken = jwtService.decodeToken(request);
			return ResponseEntity.ok(decodedToken);
		} catch (IllegalArgumentException e) {
			log.error("JWT 디코딩 실패: {}", e.getMessage());
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}

	}

	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {
		log.info("Refresh Token 을 사용하여 새로운 Access Token 발급 요청");
		return ResponseEntity.ok(jwtService.refreshAccessToken(request));
	}
}
