package com.example.codewithpraveen.banking_management_system.payLoad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
	
	@NotBlank(message = "Access token is required")
	private String accessToken;
	
	private String refreshToken;
}
