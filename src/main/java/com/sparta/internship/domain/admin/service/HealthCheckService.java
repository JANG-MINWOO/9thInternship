package com.sparta.internship.domain.admin.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

	public String getHealthStatus() {
		return "Server is running";
	}
}
