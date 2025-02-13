package com.sparta.internship.config.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.internship.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long ACCESS_TOKEN_TIME = 60000 * 60 * 1000L; // 60분
	private static final long REFRESH_TOKEN_TIME = 60000 * 60 * 24 * 7L;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	@Setter
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	// Access Token 과 Refresh Token 구분을 위해 수정
	public String createAccessToken(Long userId, String username, UserRole userRole) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("username", username)
				.claim("userRole", userRole)
				.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	// Refresh Token 생성로직
	public String createRefreshToken(Long userId, String username) {
		Date date = new Date();
		return BEARER_PREFIX +
			Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("username", username)
			.setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
			.setIssuedAt(date)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		throw new IllegalArgumentException("Not Found Token");
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	// Refresh Token 울 HTTP-Only 쿠키에 저장
	public void addRefreshTokenToCookie(String token, HttpServletResponse res) {
		try {
			token = URLEncoder.encode(token, "utf-8")
				.replaceAll("\\+", "%20");

			Cookie cookie = new Cookie("Refresh-Token", token); // 쿠키에 Refresh Token 저장
			cookie.setPath("/");
			cookie.setHttpOnly(true); // XSS 공격 방지를 위해 HTTP-Only 설정
			cookie.setSecure(true); // HTTPS 환경에서만 사용 가능하도록 설정 (운영 환경에서 필수)

			res.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			log.error("Refresh Token 쿠키 저장 실패: {}", e.getMessage());
		}
	}

	public String getTokenFromHeader(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
			return token.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	// HttpServletRequest 에서 Refresh Token 가져오기
	public String getRefreshTokenFromCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("Refresh-Token")) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Decode 처리
					} catch (UnsupportedEncodingException e) {
						log.error("Refresh Token 디코딩 실패: {}", e.getMessage());
						return null;
					}
				}
			}
		}
		return null;
	}

	//JWT 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	public boolean validateRefreshToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 Refresh Token: {}", e.getMessage());
			return false;
		}
	}

	public Key getSigningKey() {
		return this.key;
	}
}
