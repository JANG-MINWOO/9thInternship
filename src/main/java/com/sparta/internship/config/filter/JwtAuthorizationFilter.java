package com.sparta.internship.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.internship.config.jwt.JwtUtil;
import com.sparta.internship.config.security.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws
		ServletException,
		IOException {

		String tokenValue = jwtUtil.getTokenFromHeader(req);

		// Authorization 헤더가 없으면 로그만 남기고 필터 통과
		if (!StringUtils.hasText(tokenValue)) {
			log.warn("🚨 요청에 Authorization 헤더가 없음 - {}", req.getRequestURI());
			filterChain.doFilter(req, res);
			return;
		}

		try {
			// Access Token 검증
			if (!jwtUtil.validateToken(tokenValue)) {
				log.warn("🚨 Invalid Access Token - {}", req.getRequestURI());
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				res.setContentType("application/json");
				res.getWriter().write("{\"error\": \"Invalid Access Token\"}");
				return;
			}
		} catch (ExpiredJwtException e) {
			log.warn("🔄 Access Token 만료 - {}", req.getRequestURI());

			// Access Token 이 만료되었지만, Refresh Token API 는 예외적으로 허용
			if (req.getRequestURI().equals("/api/jwt/refresh")) {
				filterChain.doFilter(req, res);
				return;
			}

			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.setContentType("application/json");
			res.getWriter().write("{\"error\": \"Access Token Expired\"}");
			return;
		}

		// Access Token 이 유효하면 인증 설정 진행
		log.info("✅ Authorization 헤더에서 Access Token 확인: {}", tokenValue);

		Claims info = jwtUtil.extractClaims(tokenValue);
		String username = info.get("username", String.class);

		try {
			setAuthentication(username);
		} catch (Exception e) {
			log.error("❌ Authentication 설정 실패: {}", e.getMessage());
			return;
		}

		filterChain.doFilter(req, res);
	}

	// 인증 처리
	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
