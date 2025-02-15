package com.sparta.internship.config.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.internship.config.jwt.JwtUtil;
import com.sparta.internship.config.security.UserDetailsImpl;
import com.sparta.internship.domain.user.dto.request.SigninRequest;
import com.sparta.internship.domain.user.entity.UserRole;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
@Tag(name = "인증", description = "JWT 기반 로그인 API")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/sign");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		log.info("로그인 시도");
		try {
			SigninRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), SigninRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException {
		log.info("로그인 성공 및 JWT 생성");

		UserDetailsImpl userDetails = (UserDetailsImpl)authResult.getPrincipal();
		UserRole role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getUserRole();

		String accessToken = jwtUtil.createAccessToken(userDetails.getUser().getId(), userDetails.getUsername(), role);
		String refreshToken = jwtUtil.createRefreshToken(userDetails.getUser().getId(), userDetails.getUsername());

		// Access Token 은 Authorization 헤더로 반환하도록 설정
		response.setHeader("Authorization", accessToken);
		response.setHeader("Access-Control-Expose-Headers", "Authorization");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(
			Map.of("accessToken", accessToken, "refreshToken", refreshToken)
		));

		// Refresh Token 은 HTTP-Only 쿠키에 저장해서 보안 강화
		jwtUtil.addRefreshTokenToCookie(refreshToken, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException {
		log.info("로그인 실패");
		response.setStatus(401);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String errorMessage = "로그인에 실패했습니다. 사용자 이름 또는 비밀번호를 확인해주세요.";
		response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", errorMessage)));
	}
}
