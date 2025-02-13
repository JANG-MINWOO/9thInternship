package com.sparta.internship.config.interceptor;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getRequestURI().startsWith("/restricted-api")) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;
				if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
					String errorMessage = "접근 차단: " + userDetails.getUsername() + " (ADMIN 권한 필요)";
					log.warn(errorMessage);
					sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, errorMessage);
					return false;
				}
			} else {
				// 로그인이 안된 경우
				String errorMessage = "인증되지 않은 사용자입니다.";
				log.warn(errorMessage);
				sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
				return false;
			}
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
