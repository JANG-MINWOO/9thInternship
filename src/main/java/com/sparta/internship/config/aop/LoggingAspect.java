package com.sparta.internship.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.internship.config.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

	private final JwtUtil jwtUtil;

	@Around("execution(* com.sparta.internship.domain.admin.controller.*.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		HttpServletRequest request = extractHttpServletRequest(joinPoint.getArgs());

		if (request != null) {
			// Access Token 을 헤더에서 가져오기
			String tokenValue = jwtUtil.getTokenFromHeader(request);
			if (!StringUtils.hasText(tokenValue) || !jwtUtil.validateToken(tokenValue)) {
				log.warn("유효하지 않은 Access Token - 로깅 불가");
				return joinPoint.proceed();
			}
		}

		Object proceed = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - start;
		log.info("[{}] 실행 시간: {} ms", joinPoint.getSignature().getName(), executionTime);

		return proceed;
	}

	private HttpServletRequest extractHttpServletRequest(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				return (HttpServletRequest) arg;
			}
		}
		return null;
	}
}
