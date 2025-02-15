package com.sparta.internship.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.user.dto.request.SigninRequest;
import com.sparta.internship.domain.user.dto.request.SignupRequest;
import com.sparta.internship.domain.user.dto.response.SignupResponse;
import com.sparta.internship.domain.user.dto.response.TokenResponse;
import com.sparta.internship.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "회원가입 성공",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "username": "testuser",
                    "nickname": "테스트 유저",
                    "authorities": [
                        {
                            "authority": "ROLE_USER"
                        }
                    ]
                }
                """))),
		@ApiResponse(responseCode = "400", description = "이미 존재하는 사용자",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "error": "Username is already in use"
                }
                """)))
	})
	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
		SignupResponse response = userService.signup(signupRequest);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "사용자 로그인", description = "사용자가 아이디와 비밀번호를 입력하여 JWT Access Token과 Refresh Token을 발급받습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "accessToken": "Bearer eyJhbGciOiJIUzI1NiJ9...",
                    "refreshToken": "Bearer eyJhbGciOiJIUzI1NiJ9..."
                }
                """))),
		@ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = """
                {
                    "error": "Unauthorized"
                }
                """)))
	})
	@PostMapping("/sign")
	public ResponseEntity<TokenResponse> login(@RequestBody SigninRequest signinRequest) {
		// 실제 로그인 로직은 JwtAuthenticationFilter에서 처리됨.
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
