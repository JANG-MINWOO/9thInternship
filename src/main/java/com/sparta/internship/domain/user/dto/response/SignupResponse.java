package com.sparta.internship.domain.user.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
	private String username;
	private String nickname;
	private List<AuthorityDto> authorities;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AuthorityDto {
		private String authorityName;
	}
}
