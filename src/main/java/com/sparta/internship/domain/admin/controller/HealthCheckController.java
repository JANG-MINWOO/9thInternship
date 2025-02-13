package com.sparta.internship.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.admin.service.HealthCheckService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restricted-api")
@RequiredArgsConstructor
public class HealthCheckController {

	private final HealthCheckService healthCheckService;

	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		String status = healthCheckService.getHealthStatus();
		return ResponseEntity.ok(status);
	}

}
