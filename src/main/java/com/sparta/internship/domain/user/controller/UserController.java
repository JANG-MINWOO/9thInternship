package com.sparta.internship.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.internship.domain.user.dto.request.SignupRequest;
import com.sparta.internship.domain.user.dto.response.SignupResponse;
import com.sparta.internship.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
		SignupResponse response = userService.signup(signupRequest);
		return ResponseEntity.ok(response);
	}
}
