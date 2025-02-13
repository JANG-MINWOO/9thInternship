package com.sparta.internship.config.interceptor;

import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.internship.config.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

	private final JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Access Token 을 헤더에서 가져오기
		String tokenValue = jwtUtil.getTokenFromHeader(request);

		if (tokenValue == null || !jwtUtil.validateToken(tokenValue)) {
			log.warn("유효하지 않은 Access Token: 요청 차단");
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token");
			return false;
		}

		return true;
	}

	private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws Exception {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, String> errorResponse = Map.of(
			"status", String.valueOf(statusCode),
			"error", message
		);

		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
	}
}
