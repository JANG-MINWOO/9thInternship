package com.sparta.internship.domain.user.entity;

import java.util.Arrays;

public enum UserRole {
	ADMIN, USER;

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserRole"));
	}
}
