package com.sparta.internship.domain.token.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.token.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class JwtController {

	private final JwtService jwtService;

	@Operation(summary = "Access Token 디코딩", description = "헤더에 포함된 JWT Access Token을 디코딩하여 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "디코딩 성공",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "header": "{ 'alg': 'HS256', 'typ': 'JWT' }",
                    "payload": "{ 'sub': '1', 'username': 'testuser', 'exp': 1700000000 }",
                    "claims": {
                        "sub": "1",
                        "username": "testuser",
                        "exp": 1700000000
                    }
                }
                """))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "error": "Invalid JWT token"
                }
                """)))
	})
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

	@Operation(summary = "Refresh Token을 이용한 Access Token 재발급", description = "Refresh Token을 사용하여 새로운 Access Token을 발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "accessToken": "Bearer eyJhbGciOiJIUzI1NiJ9..."
                }
                """))),
		@ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않음",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "error": "Invalid Refresh Token"
                }
                """)))
	})
	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refreshToken(
		@RequestHeader(name = "Cookie", required = false) String cookieHeader, HttpServletRequest request) {
		log.info("Refresh Token 을 사용하여 새로운 Access Token 발급 요청");
		log.info("🔄 Refresh Token 요청 - Cookie Header: {}", cookieHeader);
		return ResponseEntity.ok(jwtService.refreshAccessToken(request));
	}
}
