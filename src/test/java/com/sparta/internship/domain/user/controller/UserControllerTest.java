package com.sparta.internship.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.internship.domain.user.dto.request.SignupRequest;
import com.sparta.internship.domain.user.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private UserService userService;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		// UserService를 Mocking
		userService = Mockito.mock(UserService.class);

		// UserController를 직접 설정
		UserController userController = new UserController(userService);

		// MockMvc 설정
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("회원가입 - 성공")
	void signupSuccess() throws Exception {
		// Given
		SignupRequest request = new SignupRequest();
		request.setUsername("test user");
		request.setPassword("Test@1234");
		request.setNickname("test nickname");

		// When & Then
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		// UserService의 signup 메서드가 호출되었는지 확인
		verify(userService, times(1)).signup(any(SignupRequest.class));
	}
}