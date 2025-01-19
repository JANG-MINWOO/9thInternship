package com.sparta.internship.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class SignupRequest {
	private String username;
	private String password;
	private String nickname;
}
