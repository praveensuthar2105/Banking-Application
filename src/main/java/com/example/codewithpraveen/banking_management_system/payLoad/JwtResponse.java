package com.example.codewithpraveen.banking_management_system.payLoad;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private UserDto user;
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private long expiresIn; // Access token expiration time in seconds
	private LocalDateTime issuedAt;
	
	// Constructor for backward compatibility (without refresh token)
	public JwtResponse(UserDto user, String accessToken) {
		this.user = user;
		this.accessToken = accessToken;
		this.tokenType = "Bearer";
		this.issuedAt = LocalDateTime.now();
	}
	
	// Constructor with access and refresh tokens
	public JwtResponse(UserDto user, String accessToken, String refreshToken, long expiresIn) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenType = "Bearer";
		this.expiresIn = expiresIn;
		this.issuedAt = LocalDateTime.now();
	}
}
