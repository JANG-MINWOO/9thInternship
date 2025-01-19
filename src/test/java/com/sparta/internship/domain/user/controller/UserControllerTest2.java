package com.sparta.internship.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.internship.config.jwt.JwtUtil;
import com.sparta.internship.domain.user.dto.request.SigninRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest2 {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("로그인 성공 테스트")
	void loginSuccess() throws Exception {
		// Given
		SigninRequest signinRequest = new SigninRequest();
		signinRequest.setUsername("MinWoo");
		signinRequest.setPassword("12341234");

		// When & Then
		mockMvc.perform(post("/sign")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signinRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	@DisplayName("로그인 실패 테스트")
	void loginFailure() throws Exception {
		// Given
		SigninRequest signinRequest = new SigninRequest();
		signinRequest.setUsername("MinWoo");
		signinRequest.setPassword("wrongpassword");

		// When & Then
		mockMvc.perform(post("/sign")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signinRequest)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error").value("로그인에 실패했습니다. 사용자 이름 또는 비밀번호를 확인해주세요."));
	}
}
