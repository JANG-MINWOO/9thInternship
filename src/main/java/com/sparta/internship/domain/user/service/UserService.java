package com.sparta.internship.domain.user.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.internship.domain.user.dto.request.SignupRequest;
import com.sparta.internship.domain.user.dto.response.SignupResponse;
import com.sparta.internship.domain.user.entity.User;
import com.sparta.internship.domain.user.entity.UserRole;
import com.sparta.internship.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponse signup(SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			throw new IllegalArgumentException("Username is already in use");
		}

		User user = User.createOf(
			signupRequest.getUsername(),
			passwordEncoder.encode(signupRequest.getPassword()),
			signupRequest.getNickname(),
			UserRole.USER);

		User savedUser = userRepository.save(user);

		return new SignupResponse(
			savedUser.getUsername(),
			savedUser.getNickname(),
			Collections.singletonList(new SignupResponse.AuthorityDto("ROLE_USER"))
		);

	}
}
