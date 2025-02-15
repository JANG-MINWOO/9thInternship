package com.sparta.internship.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.admin.service.HealthCheckService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restricted-api")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class HealthCheckController {

	private final HealthCheckService healthCheckService;

	@Operation(summary = "서버 상태 체크", description = "서버가 정상적으로 동작 중인지 확인합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "서버 정상 작동",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "status": "Server is running"
                }
                """))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "error": "Internal Server Error"
                }
                """)))
	})
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		String status = healthCheckService.getHealthStatus();
		return ResponseEntity.ok(status);
	}

}
